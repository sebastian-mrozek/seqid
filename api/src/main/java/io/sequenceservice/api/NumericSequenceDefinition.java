package io.sequenceservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class NumericSequenceDefinition {

    private final String namespace;
    private final String name;
    private final long start;
    private final Short padding;
    private final Long max;
    private final String prefix;
    private final String suffix;

    @JsonCreator
    public NumericSequenceDefinition(
            @JsonProperty("namespace") String namespace,
            @JsonProperty("name") String name,
            @JsonProperty("start") long start,
            @JsonProperty("padding") Short padding,
            @JsonProperty("max") Long max,
            @JsonProperty("prefix") String prefix,
            @JsonProperty("suffix") String suffix) {
        this.namespace = namespace;
        this.name = name;
        this.start = start;
        this.padding = padding;
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

    public Short getPadding() {
        return padding;
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
                Objects.equals(padding, that.padding) && Objects.equals(max, that.max) &&
                Objects.equals(prefix, that.prefix) && Objects.equals(suffix, that.suffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, name, start, padding, max, prefix, suffix);
    }

    @Override
    public String toString() {
        return "NumericSequenceDefinition{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", padding=" + padding +
                ", max=" + max +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
