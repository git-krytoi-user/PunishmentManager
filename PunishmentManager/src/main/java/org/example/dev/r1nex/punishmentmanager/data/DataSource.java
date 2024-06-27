package org.example.dev.r1nex.punishmentmanager.data;

import java.util.UUID;

public class DataSource {

    private final UUID uuid;
    private final String admin;
    private final long time;
    private final String type;
    private final String reason;
    private final String ip;

    public DataSource(UUID uuid, String admin, long time, String type, String reason, String ip) {
        this.uuid = uuid;
        this.admin = admin;
        this.time = time;
        this.type = type;
        this.reason = reason;
        this.ip = ip;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getAdmin() {
        return admin;
    }

    public long getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public String getIp() {
        return ip;
    }
}
