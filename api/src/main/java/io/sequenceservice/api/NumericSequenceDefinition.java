package io.sequenceservice.api;

import java.util.Objects;

public class NumericSequenceDefinition {

    private final String namespace;
    private final String name;
    private final long start;

    public NumericSequenceDefinition(String namespace, String name, long start) {
        this.namespace = namespace;
        this.name = name;
        this.start = start;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public long getStart() {
        return start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericSequenceDefinition that = (NumericSequenceDefinition) o;
        return start == that.start && namespace.equals(that.namespace) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name, start);
    }

    @Override
    public String toString() {
        return "NumericSequenceDefinition{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                ", start=" + start +
                '}';
    }
}
