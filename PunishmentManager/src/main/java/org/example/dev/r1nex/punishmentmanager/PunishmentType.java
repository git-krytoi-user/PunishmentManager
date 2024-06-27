package org.example.dev.r1nex.punishmentmanager;

public enum PunishmentType {
    FOREVER("pb.ban.use"),
    TEMPBAN("pb.tempban.use"),
    BANIP("pb.banip.use"),
    UNBAN("pb.unban.use");

    private final String perms;

    PunishmentType(String perms) {
        this.perms = perms;
    }

    public String getPerms() {
        return perms;
    }
}
