package io.sequenceservice.api;

public class NumericSequence {

    private final String id;
    private final NumericSequenceDefinition sequenceDefinition;
    private final long lastValue;

    public NumericSequence(String id, NumericSequenceDefinition sequenceDefinition, long lastValue) {
        this.id = id;
        this.sequenceDefinition = sequenceDefinition;
        this.lastValue = lastValue;
    }

    public String getId() {
        return id;
    }

    public NumericSequenceDefinition getSequenceDefinition() {
        return sequenceDefinition;
    }

    public long getLastValue() {
        return lastValue;
    }
}
