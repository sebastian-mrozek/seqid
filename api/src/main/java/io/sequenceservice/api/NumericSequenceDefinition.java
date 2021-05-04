package io.sequenceservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class NumericSequenceDefinition {

    private final String namespace;
    private final String name;
    private final long start;
    private final Short length;
    private final Long max;
    private final String prefix;
    private final String suffix;

    @JsonCreator
    public NumericSequenceDefinition(
            @JsonProperty("namespace") String namespace,
            @JsonProperty("name") String name,
            @JsonProperty("start") long start,
            @JsonProperty("length") Short length,
            @JsonProperty("max") Long max,
            @JsonProperty("prefix") String prefix,
            @JsonProperty("suffix") String suffix) {
        this.namespace = namespace;
        this.name = name;
        this.start = start;
        this.length = length;
        this.max = max;
        this.prefix = prefix;
        this.suffix = suffix;
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

    public Short getLength() {
        return length;
    }

    public Long getMax() {
        return max;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericSequenceDefinition that = (NumericSequenceDefinition) o;
        return start == that.start && namespace.equals(that.namespace) && name.equals(that.name) &&
                Objects.equals(length, that.length) && Objects.equals(max, that.max) &&
                Objects.equals(prefix, that.prefix) && Objects.equals(suffix, that.suffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name, start, length, max, prefix, suffix);
    }

    @Override
    public String toString() {
        return "NumericSequenceDefinition{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", length=" + length +
                ", max=" + max +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
