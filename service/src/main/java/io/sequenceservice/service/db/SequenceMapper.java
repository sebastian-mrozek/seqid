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

    public NumericSequence toApi(DSequenceDefinition dbSequence, long lastValue) {
        if (dbSequence == null) {
            return null;
        }

        NumericSequenceDefinition sequenceDefinition = new NumericSequenceDefinition(
                dbSequence.getNamespace(),
                dbSequence.getName(),
                dbSequence.getStart());

        return new NumericSequence(
                dbSequence.getId().toString(),
                sequenceDefinition,
                lastValue);
    }
}
