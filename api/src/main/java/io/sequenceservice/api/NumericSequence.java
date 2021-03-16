package io.sequenceservice.api;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericSequence that = (NumericSequence) o;
        return lastValue == that.lastValue && id.equals(that.id) && sequenceDefinition.equals(that.sequenceDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sequenceDefinition, lastValue);
    }

    @Override
    public String toString() {
        return "NumericSequence{" +
                "id='" + id + '\'' +
                ", sequenceDefinition=" + sequenceDefinition +
                ", lastValue=" + lastValue +
                '}';
    }
}
