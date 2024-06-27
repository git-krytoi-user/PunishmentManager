package org.example.dev.r1nex.punishmentmanager;

import org.example.dev.r1nex.punishmentmanager.commands.CommandBan;
import org.example.dev.r1nex.punishmentmanager.commands.CommandBanip;
import org.example.dev.r1nex.punishmentmanager.commands.CommandTempban;
import org.example.dev.r1nex.punishmentmanager.commands.CommandUnban;

import java.util.Objects;

public class RegisterCommands {

    public RegisterCommands(PunishmentManager plugin) {
        Objects.requireNonNull(plugin.getCommand("ban")).setExecutor(new CommandBan(plugin));
        Objects.requireNonNull(plugin.getCommand("banip")).setExecutor(new CommandBanip());
        Objects.requireNonNull(plugin.getCommand("tempban")).setExecutor(new CommandTempban(plugin));
        Objects.requireNonNull(plugin.getCommand("unban")).setExecutor(new CommandUnban(plugin));
    }
}
