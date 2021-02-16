package io.ids.service;

import io.ebean.annotation.Platform;

import java.util.UUID;

public class SequenceQueryProvider {


    private final String createQuery;
    private final String dropQuery;
    private final String incrementQuery;
    private final String selectQuery;

    private SequenceQueryProvider(String createQuery, String dropQuery, String incrementQuery, String selectQuery) {
        this.createQuery = createQuery;
        this.dropQuery = dropQuery;
        this.incrementQuery = incrementQuery;
        this.selectQuery = selectQuery;
    }

    public String getCreateQuery(UUID id, long start) {
        return String.format(this.createQuery, id, start);
    }

    public String getDropQuery(UUID id) {
        return String.format(this.dropQuery, id);
    }

    public String getIncrementQuery(UUID id) {
        return String.format(this.incrementQuery, id);
    }

    public String getSelectQuery(UUID id) {
        return String.format(this.selectQuery, id);
    }

    public static SequenceQueryProvider postgres() {
        return new SequenceQueryProvider(
                "CREATE SEQUENCE \"%s\" INCREMENT 1 START %s;",
                "DROP SEQUENCE \"%s\";",
                "SELECT nextval('%s');",
                "SELECT \"last_value\" FROM \"%s\";");
    }

    public static SequenceQueryProvider h2() {
        return new SequenceQueryProvider(
                "CREATE SEQUENCE \"%s\" INCREMENT 1 START %s;",
                "DROP SEQUENCE \"%s\";",
                "SELECT NEXT VALUE FOR \"%s\";",
                "SELECT CURRENT VALUE FOR \"%s\"");
    }

    public static SequenceQueryProvider forPlatform(Platform platform) {
        switch (platform) {
            case H2: return h2();
            case POSTGRES: return postgres();
            default: throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }
}
