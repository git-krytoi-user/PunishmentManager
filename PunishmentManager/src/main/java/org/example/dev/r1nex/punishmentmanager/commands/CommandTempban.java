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

public class CommandTempban implements CommandExecutor {

    private final PunishmentManager plugin;

    public CommandTempban(PunishmentManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(PunishmentType.TEMPBAN.getPerms())) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getNotPerms()));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getCommandTempBanUse()));
            return true;
        }

        if (args[0].isEmpty()) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getCommandTempBanUse()));
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

        String reason = plugin.getMethods().getFinalArg(args, 2);
        String time = args[1];

        if (time.length() < 2) {
            sender.sendMessage(plugin.getConfigManager().replacer(plugin.getConfigManager().getFormatDate()));
            return true;
        }

        long millis = (plugin.getMethods().getTimeFromString(time));
        String formattedTime = plugin.getMethods().getRemainingTimeFormat(offlinePlayer, millis);

        Bukkit.broadcast(Component.text(
                plugin.getConfigManager().replacer(
                plugin.getConfigManager()
                .getTempBanMessageChat()
                .replaceAll("%name%", Objects.requireNonNull(offlinePlayer.getName()))
                .replaceAll("%time%", formattedTime))
                .replaceAll("%admin%", sender.getName())
                .replaceAll("%reason%", reason))
        );

        plugin.getMySQL().insertBan(
                offlinePlayer,
                uuid,
                sender.getName(),
                reason,
                System.currentTimeMillis() + millis,
                PunishmentType.TEMPBAN.toString()
        );
        return false;
    }
}
