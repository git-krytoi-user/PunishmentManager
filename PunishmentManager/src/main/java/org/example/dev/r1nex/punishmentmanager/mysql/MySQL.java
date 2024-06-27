package org.example.dev.r1nex.punishmentmanager.mysql;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.example.dev.r1nex.punishmentmanager.MySQLQueries;
import org.example.dev.r1nex.punishmentmanager.PunishmentManager;
import org.example.dev.r1nex.punishmentmanager.data.DataSource;

import java.sql.*;
import java.util.UUID;

public class MySQL {

    private final PunishmentManager plugin;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public MySQL(String dbHost, String dbName, String dbUser, String dbPassword, PunishmentManager plugin) {
        this.plugin = plugin;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;

        dbUrl = "jdbc:mysql://" + dbHost + "/" + dbName + "?useUnicode=true&characterEncoding=utf8&autoReconnect=true";

        try (Connection ignored = openConnection()) {
            plugin.getLogger().info("Подключение к базе успешно.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            synchronized (MySQL.this) {
                runnable.run();
            }
        });
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void setData() {
        async(() -> {
            try (Connection connection = openConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(MySQLQueries.QUERY_SET_BANS.toString())) {
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String uniqueIdentifier = rs.getString("uuid");
                            String admin = rs.getString("admin");
                            String reason = rs.getString("reason");
                            String type = rs.getString("type");
                            long date = rs.getLong("time");
                            UUID uuid = UUID.fromString(uniqueIdentifier);

                            plugin.getCache().put(uuid, new DataSource(uuid, admin, date, type, reason, "0"));
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void insertBan(OfflinePlayer offlinePlayer, UUID uuid, String admin, String reason, long time, String type) {
        async(() -> {
            try (Connection connection = openConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(MySQLQueries.QUERY_INSERT.toString())) {
                    ps.setString(1, uuid.toString());
                    ps.setString(2, admin);
                    ps.setString(3, type);
                    ps.setString(4, reason);
                    ps.setLong(5, time);

                    ps.executeUpdate();

                    plugin.getCache().put(uuid, new DataSource(uuid, admin, time, type, reason, "0"));

                    plugin.getMethods().kickPlayer(offlinePlayer);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeBan(UUID uuid) {
        async(() -> {
            try (Connection connection = openConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(MySQLQueries.QUERY_REMOVE_BAN.toString())) {
                    ps.setString(1, uuid.toString());
                    ps.executeUpdate();

                    plugin.getCache().remove(uuid);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
