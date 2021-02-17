package io.sequenceservice.api;

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
}
