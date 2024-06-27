package org.example.dev.r1nex.punishmentmanager.commands;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.example.dev.r1nex.punishmentmanager.PunishmentManager;
import org.example.dev.r1nex.punishmentmanager.PunishmentType;
import org.example.dev.r1nex.punishmentmanager.data.DataSource;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class CommandUnban implements CommandExecutor {

    private final PunishmentManager plugin;

    public CommandUnban(PunishmentManager plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(PunishmentType.UNBAN.getPerms())) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getNotPerms()));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getCommandUnbanUse()));
            return true;
        }

        if (args[0].isEmpty()) {
            player.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getUnbanMessageChat()));
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);

        if (offlinePlayer == null) {
            player.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getNotPlayingPlayer()));
            return true;
        }

        UUID uuid = offlinePlayer.getUniqueId();

        if (!plugin.getCache().containsKey(uuid)) {
            player.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getNotBanned()));
            return true;
        }

        DataSource dataSource = plugin.getCache().get(uuid);

        String admin = dataSource.getAdmin();
        OfflinePlayer offlinePlayerAdmin = Bukkit.getOfflinePlayerIfCached(admin);

        assert offlinePlayerAdmin != null;
        if (plugin.getMethods().isOfflinePlayerStaff(offlinePlayerAdmin) && !plugin.getMethods().isOnlinePlayerStaff(player)) {
            player.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getUnBanOwn()));
            return true;
        }

        Bukkit.broadcast(Component.text(plugin.getConfigManager()
                .replacer(plugin.getConfigManager().getUnbanMessageChat()
                .replaceAll("%name%", Objects.requireNonNull(offlinePlayer.getName()))
                .replaceAll("%admin%", sender.getName()))
        ));

        plugin.getMySQL().removeBan(uuid);
        return false;
    }
}
