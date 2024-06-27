package org.example.dev.r1nex.punishmentmanager;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.dev.r1nex.punishmentmanager.configs.ConfigManager;
import org.example.dev.r1nex.punishmentmanager.data.DataSource;
import org.example.dev.r1nex.punishmentmanager.listeners.Listeners;
import org.example.dev.r1nex.punishmentmanager.methods.Methods;
import org.example.dev.r1nex.punishmentmanager.mysql.MySQL;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public final class PunishmentManager extends JavaPlugin {

    private ConfigManager configManager;
    private MySQL mySQL;
    private Methods methods;
    private LuckPerms luckPerms;

    private final HashMap<UUID, DataSource> cache = new HashMap<>();

    @Override
    public void onEnable() {
        new RegisterCommands(this);
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        configManager = new ConfigManager(this);
        methods = new Methods(this);
        mySQL = new MySQL(
                configManager.getMySQLHost(),
                configManager.getMySQLdbName(),
                configManager.getMySQLdbUser(),
                configManager.getMySQLdbPassword(),
                this
        );
        mySQL.setData();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }
    }

    @Override
    public void onDisable() {
        cache.clear();
        try {
            mySQL.openConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method is used to obtain a hash map of player data.
     * @return HashMap
     */
    public HashMap<UUID, DataSource> getCache() {
        return cache;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Methods getMethods() {
        return methods;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }
}
