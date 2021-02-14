package io.ids.api;

public interface IIDService {

    NumericSequence createSequence(NumericSequenceDefinition sequenceDefinition);

    NumericSequence resetSequence(String id);

    NumericSequence resetSequence(String id, long start);

    void deleteSequence(String id);

    NumericSequence getSequence(String namespace, String mame);

    NumericSequence getSequence(String id);

    long increment(String namespace, String name);

    long increment(String id);
}
