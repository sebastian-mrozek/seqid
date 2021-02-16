package io.ids.service;

import io.ebean.DB;
import io.ebean.SqlQuery;
import io.ebean.annotation.Platform;

import javax.persistence.PersistenceException;
import java.util.UUID;

public abstract class Sequencer implements ISequencer {

    public static Sequencer forPlatform(Platform platform) {
        SequenceQueryProvider queryProvider = SequenceQueryProvider.forPlatform(platform);
        switch (platform) {
            case POSTGRES:
                return new PostgresSequencer(queryProvider);
            case H2:
                return new H2Sequencer(queryProvider);
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    protected final SequenceQueryProvider queryProvider;

    private Sequencer(SequenceQueryProvider queryProvider) {
        this.queryProvider = queryProvider;
    }

    @Override
    public void create(UUID id, long start) {
        String createSequenceSql = queryProvider.getCreateQuery(id, start);
        DB.sqlUpdate(createSequenceSql).execute();
    }

    @Override
    public long next(UUID id) {
        try {
            String sql = queryProvider.getIncrementQuery(id);
            SqlQuery.TypeQuery<Long> nextValQuery = DB.sqlQuery(sql).mapToScalar(Long.class);
            return nextValQuery.findOne();
        } catch (PersistenceException e) {
            throw new IllegalArgumentException("Sequence with ID: " + id + " does not exist");
        }
    }

    @Override
    public void reset(UUID id, long start) {
        delete(id);
        create(id, start);
    }

    @Override
    public void delete(UUID id) {
        try {
            String sql = queryProvider.getDropQuery(id);
            DB.sqlUpdate(sql).execute();
        } catch (PersistenceException e) {
            throw new IllegalArgumentException("Sequence with ID: " + id + " does not exist");
        }
    }

    private static class PostgresSequencer extends Sequencer {

        private PostgresSequencer(SequenceQueryProvider queryProvider) {
            super(queryProvider);
        }

        @Override
        public long getCurrent(UUID id) {
            try {
                String sql = queryProvider.getSelectQuery(id);
                SqlQuery.TypeQuery<Long> lastValueQuery = DB.sqlQuery(sql).mapToScalar(Long.class);
                return lastValueQuery.findOne();
            } catch (PersistenceException e) {
                throw new IllegalArgumentException("Sequence with ID: " + id + " does not exist");
            }
        }
    }

    private static class H2Sequencer extends Sequencer {

        private static String INFO_SCHEMA_QUERY = "SELECT CURRENT_VALUE + INCREMENT FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_NAME='%s';";

        private H2Sequencer(SequenceQueryProvider queryProvider) {
            super(queryProvider);
        }

        @Override
        public long getCurrent(UUID id) {
            try {
                String createSequenceSql = queryProvider.getSelectQuery(id);
                SqlQuery.TypeQuery<Long> lastValueQuery = DB.sqlQuery(createSequenceSql).mapToScalar(Long.class);
                return lastValueQuery.findOne();
            } catch (PersistenceException e1) {
                try {
                    String sql = String.format(INFO_SCHEMA_QUERY, id);
                    SqlQuery.TypeQuery<Long> lastValueQuery = DB.sqlQuery(sql).mapToScalar(Long.class);
                    return lastValueQuery.findOne();
                } catch (PersistenceException e2) {
                    throw new RuntimeException("Unable to fetch current value for sequence id: " + id, e2);
                }
            }
        }
    }
}
