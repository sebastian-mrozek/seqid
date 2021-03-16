package io.sequenceservice.service.db;

import io.sequenceservice.api.NumericSequence;
import io.sequenceservice.api.NumericSequenceDefinition;
import io.sequenceservice.service.db.DSequenceDefinition;

public class SequenceMapper {
    public DSequenceDefinition toDb(NumericSequenceDefinition definition) {
        if (definition == null) {
            return null;
        }

        return new DSequenceDefinition(
                definition.getNamespace(),
                definition.getName(),
                definition.getStart());
    }

    public NumericSequenceDefinition toApi(DSequenceDefinition dbSequence) {
        if (dbSequence == null) {
            return null;
        }

        return new NumericSequenceDefinition(
                dbSequence.getNamespace(),
                dbSequence.getName(),
                dbSequence.getStart());
    }

    public NumericSequence toApi(DSequenceDefinition dbSequence, long lastValue) {
        NumericSequenceDefinition sequenceDefinition = toApi(dbSequence);
        return new NumericSequence(
                dbSequence.getId().toString(),
                sequenceDefinition,
                lastValue);
    }
}
