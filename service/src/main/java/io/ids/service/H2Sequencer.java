package io.ids.service;

import io.ebean.DB;
import io.ebean.SqlQuery;

import javax.persistence.PersistenceException;
import java.util.UUID;

class H2Sequencer extends Sequencer {

    H2Sequencer(SequenceQueryProvider queryProvider) {
        super(queryProvider);
    }

    @Override
    public long getCurrent(UUID id) {
        try {
            String lastValueSql = queryProvider.getCurrentValueQuery(id);
            SqlQuery.TypeQuery<Long> lastValueQuery = DB.sqlQuery(lastValueSql).mapToScalar(Long.class);
            return lastValueQuery.findOne();
        } catch (PersistenceException e1) {
            try {
                String lastValueInfoSchemaSql = queryProvider.getCurrentValueInfoSchemaQuery(id);
                SqlQuery.TypeQuery<Long> lastValueInfoSchemaQuery = DB.sqlQuery(lastValueInfoSchemaSql).mapToScalar(Long.class);
                return lastValueInfoSchemaQuery.findOne();
            } catch (PersistenceException e2) {
                throw new RuntimeException("Unable to fetch current value for sequence id: " + id, e2);
            }
        }
    }
}
