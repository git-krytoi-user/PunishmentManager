package org.example.dev.r1nex.punishmentmanager.configs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.example.dev.r1nex.punishmentmanager.PunishmentManager;

import java.io.File;
import java.util.List;

public class ConfigManager {

    private final FileConfiguration fileConfiguration;

    public ConfigManager(PunishmentManager plugin) {
        if (!new File(plugin.getDataFolder() + File.separator + "config.yml").exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveDefaultConfig();
        }
        this.fileConfiguration = plugin.getConfig();
    }

    public String replacer(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public String getMySQLHost() {
        return fileConfiguration.getString("MySQL.dbHost");
    }

    public String getMySQLdbName() {
        return fileConfiguration.getString("MySQL.dbName");
    }

    public String getMySQLdbUser() {
        return fileConfiguration.getString("MySQL.dbUser");
    }

    public String getMySQLdbPassword() {
        return fileConfiguration.getString("MySQL.dbPassword");
    }

    public String getNotPerms() {
        return fileConfiguration.getString("Messages.notPerms");
    }

    public String getIsBanned() {
        return fileConfiguration.getString("Messages.isBanned");
    }

    public String getNotBanned() {
        return fileConfiguration.getString("Messages.notBanned");
    }

    public String getCommandBanUse() {
        return fileConfiguration.getString("Usage.banCommand");
    }

    public String getCommandUnbanUse() {
        return fileConfiguration.getString("Usage.unbanCommand");
    }

    public String getCommandTempBanUse() {
        return fileConfiguration.getString("Usage.tempbanCommand");
    }

    public String getCommandBanipUse() {
        return fileConfiguration.getString("Usage.banipCommand");
    }

    public String getDefaultReason() {
        return fileConfiguration.getString("Messages.defaultReason");
    }

    public String getBanMessageChat() {
        return fileConfiguration.getString("MessagesChat.ban");
    }

    public String getUnbanMessageChat() {
        return fileConfiguration.getString("MessagesChat.unban");
    }

    public String getBanipMessageChat() {
        return fileConfiguration.getString("MessagesChat.banip");
    }

    public String getTempBanMessageChat() {
        return fileConfiguration.getString("MessagesChat.tempban");
    }

    public List<String> listGroups() {
        return fileConfiguration.getStringList("groups");
    }

    public String getUnBanOwn() {
        return fileConfiguration.getString("Messages.unban-own");
    }

    public String getNotPlayingPlayer() {
        return fileConfiguration.getString("Messages.notPlayingPlayer");
    }

    public String getBanMessageConnect() {
        return fileConfiguration.getString("MessagesConnect.ban");
    }

    public String getBanipMessageConnect() {
        return fileConfiguration.getString("MessagesConnect.banip");
    }

    public String getFormatDate() {
        return fileConfiguration.getString("Messages.formatDate");
    }

    public String getTempBanMessageConnect() {
        return fileConfiguration.getString("MessagesConnect.tempban");
    }

    public String getExpireEmpty() {
        return fileConfiguration.getString("Messages.expireEmpty");
    }
}
