package io.sequenceservice.service.sequence;

import io.ebean.DB;
import io.ebean.SqlQuery;
import io.sequenceservice.api.NumericSequenceDefinition;

import javax.persistence.PersistenceException;
import java.util.UUID;

abstract class Sequencer implements ISequencer {

    protected final SequenceQueryProvider queryProvider;

    Sequencer(SequenceQueryProvider queryProvider) {
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
            String nextValueSql = queryProvider.getNextValueQuery(id);
            SqlQuery.TypeQuery<Long> nextValueQuery = DB.sqlQuery(nextValueSql).mapToScalar(Long.class);
            return nextValueQuery.findOne();
        } catch (PersistenceException e) {
            throw new SequenceNotFoundException(id.toString());
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
            String dropSql = queryProvider.getDropQuery(id);
            DB.sqlUpdate(dropSql).execute();
        } catch (PersistenceException e) {
            throw new SequenceNotFoundException(id.toString());
        }
    }
}
