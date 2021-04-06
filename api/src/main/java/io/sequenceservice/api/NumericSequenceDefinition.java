package io.sequenceservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class NumericSequenceDefinition {

    private final String namespace;
    private final String name;
    private final long start;

    @JsonCreator
    public NumericSequenceDefinition(@JsonProperty("namespace") String namespace, @JsonProperty("name") String name, @JsonProperty("start") long start) {
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
