package io.sequenceservice.service.sequence;

import io.ebean.annotation.Platform;

import java.util.UUID;

public class SequenceQueryProvider {


    private final String createQuery;
    private final String dropQuery;
    private final String nextValueQuery;
    private final String currentValueQuery;
    private final String currentValueInfoSchemaQuery;

    private SequenceQueryProvider(String createQuery, String dropQuery, String nextValueQuery, String currentValueQuery, String currentValueInfoSchemaQuery) {
        this.createQuery = createQuery;
        this.dropQuery = dropQuery;
        this.nextValueQuery = nextValueQuery;
        this.currentValueQuery = currentValueQuery;
        this.currentValueInfoSchemaQuery = currentValueInfoSchemaQuery;
    }

    public String getCreateQuery(UUID id, long start) {
        return String.format(this.createQuery, id, start);
    }

    public String getDropQuery(UUID id) {
        return String.format(this.dropQuery, id);
    }

    public String getNextValueQuery(UUID id) {
        return String.format(this.nextValueQuery, id);
    }

    public String getCurrentValueQuery(UUID id) {
        return String.format(this.currentValueQuery, id);
    }

    public String getCurrentValueInfoSchemaQuery(UUID id) {
        return String.format(this.currentValueInfoSchemaQuery, id);
    }

    public static SequenceQueryProvider postgres() {
        return new SequenceQueryProvider(
                "CREATE SEQUENCE \"%s\" INCREMENT 1 START %s;",
                "DROP SEQUENCE \"%s\";",
                "SELECT nextval('%s');",
                "SELECT \"last_value\" FROM \"%s\";",
                null);
    }

    public static SequenceQueryProvider h2() {
        return new SequenceQueryProvider(
                "CREATE SEQUENCE \"%s\" INCREMENT 1 START %s;",
                "DROP SEQUENCE \"%s\";",
                "SELECT NEXT VALUE FOR \"%s\";",
                "SELECT CURRENT VALUE FOR \"%s\"",
                "SELECT CURRENT_VALUE FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_NAME='%s';");
    }

    public static SequenceQueryProvider forPlatform(Platform platform) {
        switch (platform) {
            case H2: return h2();
            case POSTGRES: return postgres();
            default: throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }
}
