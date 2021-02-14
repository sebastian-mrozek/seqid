package io.ids.api;

public class NumericSequence {

    private final String id;
    private final NumericSequenceDefinition sequenceDefinition;
    private final long current;

    public NumericSequence(String id, NumericSequenceDefinition sequenceDefinition, long current) {
        this.id = id;
        this.sequenceDefinition = sequenceDefinition;
        this.current = current;
    }

    public String getId() {
        return id;
    }

    public NumericSequenceDefinition getSequenceDefinition() {
        return sequenceDefinition;
    }

    public long getNext() {
        return current;
    }
}
