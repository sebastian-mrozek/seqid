package io.sequenceservice.api;

import java.util.List;

public interface ISequenceService {

    NumericSequence createSequence(NumericSequenceDefinition sequenceDefinition);

    NumericSequence resetSequence(String id);

    NumericSequence resetSequence(String id, long start);

    void deleteSequence(String id);

    NumericSequence getSequence(String namespace, String mame);

    NumericSequence getSequence(String id);

    long increment(String namespace, String name);

    long increment(String id);

    List<NumericSequenceDefinition> listAllDefinitions();

    List<NumericSequenceDefinition> listDefinitionsByNamespace(String namespace);

    List<NumericSequence> listAllSequences();

    List<NumericSequence> listSequencesByNamespace(String namespace);
}
