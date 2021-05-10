package io.sequenceservice.service;

import io.sequenceservice.api.ISequenceService;
import io.sequenceservice.api.NumericSequence;
import io.sequenceservice.api.NumericSequenceDefinition;
import io.sequenceservice.service.db.query.QDSequenceDefinition;
import io.sequenceservice.service.sequence.SequenceNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RDBSequenceServiceTest {

    private final ISequenceService sequenceService = RDBSequenceService.newInstance();

    @AfterEach
    public void cleanup() {
        new QDSequenceDefinition().delete();
    }

    @Test
    public void testCreateAndGet() {
        NumericSequence sequence = sequenceService.createSequence(new NumericSequenceDefinition("ns", "seq1", 5, (short) 3, Long.MAX_VALUE, "INV-", "-OK"));
        assertSequence(sequence, "ns", "seq1", 5, (short) 3, Long.MAX_VALUE, "INV-", "-OK", null);

        sequence = sequenceService.getSequence("ns", "seq1");
        assertSequence(sequence, "ns", "seq1", 5, (short) 3, Long.MAX_VALUE, "INV-", "-OK", null);
    }

    @Test
    public void testNext() {
        String id = sequenceService.createSequence(new NumericSequenceDefinition("ns2", "seq5", 11, (short) 3, 98989898989L, null, null)).getId();

        AtomicReference<String> lastValue = new AtomicReference<>(null);
        Stream.of("011", "012", "013", "014").forEach(expectedNext -> {
            NumericSequence sequence = sequenceService.getSequence(id);
            assertSequence(sequence, "ns2", "seq5", 11, (short) 3, 98989898989L, null, null, lastValue.get());
            String nextValue = sequenceService.increment(sequence.getId());
            assertEquals(expectedNext, nextValue, "Incremented value");
            lastValue.set(nextValue);
        });
    }

    @Test
    public void testDelete() {
        NumericSequence sequence = sequenceService.createSequence(new NumericSequenceDefinition("ns3", "seq123", 999, (short) 5, Long.MAX_VALUE, "O", "X"));
        assertSequence(sequence, "ns3", "seq123", 999, (short) 5, Long.MAX_VALUE, "O", "X", null);

        sequenceService.deleteSequence(sequence.getId());

        assertThrows(SequenceNotFoundException.class, () -> sequenceService.getSequence(sequence.getId()));

        assertThrows(SequenceNotFoundException.class, () -> sequenceService.getSequence("ns3", "seq123"));

        assertThrows(SequenceNotFoundException.class, () -> sequenceService.increment(sequence.getId()));

        assertThrows(SequenceNotFoundException.class, () -> sequenceService.resetSequence(sequence.getId()));
    }

    @Test
    public void testReset() {
        String id = sequenceService.createSequence(new NumericSequenceDefinition("namespaceReset", "seq999", 12345, (short) 3, Long.MAX_VALUE, "A", "Z")).getId();
        sequenceService.increment(id);
        sequenceService.increment(id);
        sequenceService.increment(id);
        String currentValue = sequenceService.increment(id);
        assertEquals("A12348Z", currentValue, "after 3 increments");

        sequenceService.resetSequence(id);
        NumericSequence sequence = sequenceService.getSequence(id);
        assertNull(sequence.getLastValue(), "last value");
        assertEquals("A12345Z", sequenceService.increment(id), "incremented 1st time after reset");
    }

    @Test
    public void testResetWithValue() {
        String id = sequenceService.createSequence(new NumericSequenceDefinition("namespaceReset", "seq999", 12345, (short) 5, Long.MAX_VALUE, "A", "Z")).getId();

        sequenceService.resetSequence(id, 98765);
        NumericSequence sequence = sequenceService.getSequence(id);
        assertEquals(98765, sequence.getSequenceDefinition().getStart());
        assertNull( sequence.getLastValue(), "last value");
        assertEquals("A98765Z", sequenceService.increment(id), "incremented 1st time");
        assertEquals("A98766Z", sequenceService.increment(id), "incremented 2nd time");
        assertEquals("A98767Z", sequenceService.increment(id), "incremented 3rd time");
    }

    @Test
    public void testListDefinitions() {
        NumericSequenceDefinition d1 = sequenceService.createSequence(new NumericSequenceDefinition("ns1", "seq1", 1, (short) 3, Long.MAX_VALUE, "A", "Z")).getSequenceDefinition();
        NumericSequenceDefinition d2 = sequenceService.createSequence(new NumericSequenceDefinition("ns1", "seq2", 2, (short) 3, Long.MAX_VALUE, "A", "Z")).getSequenceDefinition();
        NumericSequenceDefinition d3 = sequenceService.createSequence(new NumericSequenceDefinition("ns2", "seq1", 3, (short) 3, Long.MAX_VALUE, "A", "Z")).getSequenceDefinition();
        NumericSequenceDefinition d4 = sequenceService.createSequence(new NumericSequenceDefinition("ns3", "seq1", 4, (short) 3, Long.MAX_VALUE, "A", "Z")).getSequenceDefinition();
        NumericSequenceDefinition d5 = sequenceService.createSequence(new NumericSequenceDefinition("ns3", "seq2", 5, (short) 3, Long.MAX_VALUE, "A", "Z")).getSequenceDefinition();

        List<NumericSequenceDefinition> allDefinitions = sequenceService.listAllDefinitions();
        Assertions.assertThat(allDefinitions).containsExactlyInAnyOrder(d1, d2, d3, d4, d5);

        List<NumericSequenceDefinition> ns1Definitions = sequenceService.listDefinitionsByNamespace("ns1");
        Assertions.assertThat(ns1Definitions).containsExactlyInAnyOrder(d1, d2);

        List<NumericSequenceDefinition> ns2Definitions = sequenceService.listDefinitionsByNamespace("ns2");
        Assertions.assertThat(ns2Definitions).containsExactlyInAnyOrder(d3);

        List<NumericSequenceDefinition> ns3Definitions = sequenceService.listDefinitionsByNamespace("ns3");
        Assertions.assertThat(ns3Definitions).containsExactlyInAnyOrder(d4, d5);
    }

    @Test
    public void testListSequences() {
        NumericSequence s1 = sequenceService.createSequence(new NumericSequenceDefinition("ns1", "seq1", 1, (short) 3, Long.MAX_VALUE, "A", "Z"));
        NumericSequence s2 = sequenceService.createSequence(new NumericSequenceDefinition("ns1", "seq2", 2, (short) 3, Long.MAX_VALUE, "A", "Z"));
        NumericSequence s3 = sequenceService.createSequence(new NumericSequenceDefinition("ns2", "seq1", 3, (short) 3, Long.MAX_VALUE, "A", "Z"));

        List<NumericSequence> allSequences = sequenceService.listAllSequences();
        Assertions.assertThat(allSequences).containsExactlyInAnyOrder(s1, s2, s3);

        List<NumericSequence> ns1Sequences = sequenceService.listSequencesByNamespace("ns1");
        Assertions.assertThat(ns1Sequences).containsExactlyInAnyOrder(s1, s2);

        List<NumericSequence> ns2Sequences = sequenceService.listSequencesByNamespace("ns2");
        Assertions.assertThat(ns2Sequences).containsExactlyInAnyOrder(s3);
    }

    private void assertSequence(NumericSequence sequence, String namespace, String name, long start, short padding, long max, String prefix, String suffix, String lastValue) {
        assertNotNull(sequence);
        assertEquals(lastValue, sequence.getLastValue(), "last value");

        NumericSequenceDefinition sequenceDefinition = sequence.getSequenceDefinition();
        assertNotNull(sequenceDefinition);
        assertEquals(namespace, sequenceDefinition.getNamespace(), "namespace");
        assertEquals(name, sequenceDefinition.getName(), "name");
        assertEquals(start, sequenceDefinition.getStart(), "start");
        assertEquals(padding, sequenceDefinition.getPadding(), "padding");
        assertEquals(max, sequenceDefinition.getMax(), "max");
        assertEquals(prefix, sequenceDefinition.getPrefix(), "prefix");
        assertEquals(suffix, sequenceDefinition.getSuffix(), "suffix");
    }
}
