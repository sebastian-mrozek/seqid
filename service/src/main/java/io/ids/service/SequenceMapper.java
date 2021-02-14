package io.ids.service;

import io.ids.api.NumericSequence;
import io.ids.api.NumericSequenceDefinition;
import io.ids.service.db.DSequenceDefinition;

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

    public NumericSequence toApi(DSequenceDefinition dbSequence, long currentValue) {
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
                currentValue);
    }
}
