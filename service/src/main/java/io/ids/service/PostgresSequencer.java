package io.ids.service;

import io.ebean.DB;
import io.ebean.SqlQuery;

import javax.persistence.PersistenceException;
import java.util.UUID;

class PostgresSequencer extends Sequencer {

    PostgresSequencer(SequenceQueryProvider queryProvider) {
        super(queryProvider);
    }

    @Override
    public long getCurrent(UUID id) {
        try {
            String lastValueSql = queryProvider.getCurrentValueQuery(id);
            SqlQuery.TypeQuery<Long> lastValueQuery = DB.sqlQuery(lastValueSql).mapToScalar(Long.class);
            return lastValueQuery.findOne();
        } catch (PersistenceException e) {
            throw new IllegalArgumentException("Sequence with ID: " + id + " does not exist");
        }
    }
}
