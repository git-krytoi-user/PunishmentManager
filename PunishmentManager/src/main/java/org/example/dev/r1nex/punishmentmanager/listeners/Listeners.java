package org.example.dev.r1nex.punishmentmanager.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.example.dev.r1nex.punishmentmanager.PunishmentManager;
import org.example.dev.r1nex.punishmentmanager.PunishmentType;
import org.example.dev.r1nex.punishmentmanager.data.DataSource;

import java.util.Objects;
import java.util.UUID;

public class Listeners implements Listener {

    private final PunishmentManager plugin;

    public Listeners(PunishmentManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(event.getName());
        if (offlinePlayer == null) return;

        if (plugin.getCache().containsKey(uuid)) {
            DataSource dataSource = plugin.getCache().get(uuid);

            if (Objects.equals(dataSource.getType(), PunishmentType.FOREVER.toString())) {
                String kickMessage =
                        plugin.getConfigManager().replacer(
                        plugin
                        .getConfigManager()
                        .getBanMessageConnect()
                        .replaceAll("%admin%", dataSource.getAdmin())
                        .replaceAll("%reason%", dataSource.getReason())
                );

                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);

                event.kickMessage(Component.text(kickMessage));
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

                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);

                event.kickMessage(Component.text(kickMessage));
            }
        }
    }
}
