package org.example.dev.r1nex.punishmentmanager;

public enum MySQLQueries {
    QUERY_INSERT(
            "INSERT INTO bans2 (" +
                    "uuid, " +
                    "admin, " +
                    "type, " +
                    "reason, " +
                    "time) " +
                    "VALUES (" +
                    "?, " +
                    "?, " +
                    "?, " +
                    "?, " +
                    "?)"
    ),
    QUERY_SET_BANS(
            "SELECT " +
                    "uuid, " +
                    "admin, " +
                    "time, " +
                    "type, " +
                    "reason " +
                    "FROM bans2 " +
                    "ORDER BY id ASC"
    ),

    QUERY_REMOVE_BAN("DELETE FROM bans2 WHERE uuid = ?"
    ),

    QUERY_SET_IPBans("SELECT ip, reason, admin FROM ipbans ORDER BY id ASC"),

    QUERY_INSERT_IPBan("INSERT INTO ipbans " +
            "(ip, reason, admin) VALUES " +
            "(?, ?, ?)"
    );

    private final String s;

    MySQLQueries(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
