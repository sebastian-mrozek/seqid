package io.sequenceservice.service.db;

import io.sequenceservice.api.NumericSequence;
import io.sequenceservice.api.NumericSequenceDefinition;

public class SequenceMapper {
    public DSequenceDefinition toDb(NumericSequenceDefinition definition) {
        if (definition == null) {
            return null;
        }

        return new DSequenceDefinition(
                definition.getNamespace(),
                definition.getName(),
                definition.getStart(),
                definition.getPadding(),
                definition.getMax(),
                definition.getPrefix(),
                definition.getSuffix());
    }

    public NumericSequenceDefinition toApi(DSequenceDefinition dbSequence) {
        if (dbSequence == null) {
            return null;
        }

        return new NumericSequenceDefinition(
                dbSequence.getNamespace(),
                dbSequence.getName(),
                dbSequence.getStart(),
                dbSequence.getPadding(),
                dbSequence.getMax(),
                dbSequence.getPrefix(),
                dbSequence.getSuffix());
    }

    public NumericSequence toApi(DSequenceDefinition dbSequence, String lastValue) {
        NumericSequenceDefinition sequenceDefinition = toApi(dbSequence);
        return new NumericSequence(
                dbSequence.getId().toString(),
                sequenceDefinition,
                lastValue);
    }
}
