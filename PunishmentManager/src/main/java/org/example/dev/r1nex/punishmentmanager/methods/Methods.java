package org.example.dev.r1nex.punishmentmanager.methods;

import net.kyori.adventure.text.Component;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.example.dev.r1nex.punishmentmanager.PunishmentManager;
import org.example.dev.r1nex.punishmentmanager.PunishmentType;
import org.example.dev.r1nex.punishmentmanager.data.DataSource;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Methods {

    private final PunishmentManager plugin;

    public Methods(PunishmentManager plugin) {
        this.plugin = plugin;
    }

    public String getFinalArg(String[] args, int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            builder.append(args[i]);
            if (i < args.length - 1) builder.append(' ');
        }

        return builder.toString();
    }

    public long getTimeFromString(String string) {
        long time = 0;
        long temp = 0;

        char[] chars = string.toLowerCase().toCharArray();
        for (char symbol : chars) {
            if (symbol >= '0' && symbol <= '9') {
                temp = (temp * 10) + Character.digit(symbol, 10);
                continue;
            }

            switch (symbol) {
                case 's':
                    time += temp * 1000;
                    break;

                case 'm':
                    time += temp * 60 * 1000;
                    break;

                case 'h':
                    time += temp * 60 * 60 * 1000;
                    break;

                case 'd':
                    time += temp * 60 * 60 * 24 * 1000;
                    break;

            }

            temp = 0;
        }

        return time;
    }

    public void kickPlayer(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                UUID uuid = offlinePlayer.getUniqueId();
                DataSource dataSource = plugin.getCache().get(uuid);

                if (Objects.equals(dataSource.getType(), PunishmentType.FOREVER.toString())) {
                    String kickMessage =
                            plugin.getConfigManager()
                            .replacer(
                            plugin.getConfigManager()
                            .getBanMessageConnect()
                            .replaceAll("%admin%", dataSource.getAdmin())
                            .replaceAll("%reason%", dataSource.getReason())
                    );

                    Objects.requireNonNull(offlinePlayer.getPlayer()).kick(Component.text(kickMessage));
                }

                if (Objects.equals(dataSource.getType(), PunishmentType.TEMPBAN.toString())) {
                    long expire_time = dataSource.getTime();
                    String formattedTime = plugin.getMethods().getRemainingTimeFormat(
                            offlinePlayer, expire_time - System.currentTimeMillis()
                    );

                    String kickMessage =
                            plugin.getConfigManager()
                            .replacer(
                            plugin.getConfigManager()
                            .getTempBanMessageConnect()
                            .replaceAll("%admin%", dataSource.getAdmin())
                            .replaceAll("%reason%", dataSource.getReason())
                            .replaceAll("%time%", formattedTime)
                    );

                    Objects.requireNonNull(offlinePlayer.getPlayer()).kick(Component.text(kickMessage));
                }
            }, 1L);
        }
    }

    public String getRemainingTimeFormat(OfflinePlayer offlinePlayer, long time) {
        long second = (time / 1000) % 60;
        long minute = ((time / (1000 * 60)) % 60);
        long hour = ((time / (1000 * 60 * 60)) % 24);
        long day = (time / 86400000);

        if (second <= 0 && minute <= 0 && hour <= 0 && day <= 0) {
            UUID uuid = offlinePlayer.getUniqueId();
            plugin.getMySQL().removeBan(uuid);
            return plugin.getConfigManager().replacer(plugin.getConfigManager().getExpireEmpty());
        }

        StringBuilder sb = new StringBuilder();
        if(day > 0) {
            sb.append(day).append(" дн. ");
        }

        if(hour > 0) {
            sb.append(hour).append(" ч. ");
        }

        if(minute > 0) {
            sb.append(minute).append(" мин. ");
        }

        if(second > 0) {
            sb.append(second).append(" с.");
        }

        return sb.toString().trim();
    }

    public boolean isOfflinePlayerStaff(OfflinePlayer offlinePlayer) {
        UserManager userManager = plugin.getLuckPerms().getUserManager();
        CompletableFuture<User> userCompletableFuture = userManager.loadUser(offlinePlayer.getUniqueId());
        List<String> list = plugin.getConfigManager().listGroups();

        try {
            return list.contains(userCompletableFuture.get().getPrimaryGroup());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isOnlinePlayerStaff(Player player) {
        UserManager userManager = plugin.getLuckPerms().getUserManager();
        CompletableFuture<User> userCompletableFuture = userManager.loadUser(player.getUniqueId());
        List<String> list = plugin.getConfigManager().listGroups();

        try {
            return list.contains(userCompletableFuture.get().getPrimaryGroup());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
