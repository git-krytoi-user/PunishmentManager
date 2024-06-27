package org.example.dev.r1nex.punishmentmanager.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.example.dev.r1nex.punishmentmanager.PunishmentManager;
import org.example.dev.r1nex.punishmentmanager.PunishmentType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class CommandBan implements CommandExecutor {

    private final PunishmentManager plugin;

    public CommandBan(PunishmentManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(PunishmentType.FOREVER.getPerms())) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getNotPerms()));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getCommandBanUse()));
            return true;
        }

        if (args[0].length() == 0) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getCommandBanUse()));
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (offlinePlayer == null) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getNotPlayingPlayer()));
            return true;
        }

        UUID uuid = offlinePlayer.getUniqueId();

        if (plugin.getCache().containsKey(uuid)) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getIsBanned()));
            return true;
        }

        String reason = plugin.getMethods().getFinalArg(args, 1);

        if (reason.length() < 1) {
            reason = plugin.getConfigManager().replacer(plugin.getConfigManager().getDefaultReason());
        }

        Bukkit.broadcast(
                Component.text(
                plugin.getConfigManager().replacer(
                plugin.getConfigManager().getBanMessageChat()
                .replaceAll("%name%", Objects.requireNonNull(offlinePlayer.getName()))
                .replaceAll("%admin%", sender.getName())
                .replaceAll("%reason%", reason)))
        );

        plugin.getMySQL().insertBan(offlinePlayer, uuid, sender.getName(), reason, 0, PunishmentType.FOREVER.toString());


        return false;
    }
}
