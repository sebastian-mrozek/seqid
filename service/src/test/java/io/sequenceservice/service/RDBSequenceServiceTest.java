package io.sequenceservice.service;

import io.sequenceservice.api.ISequenceService;
import io.sequenceservice.api.NumericSequence;
import io.sequenceservice.api.NumericSequenceDefinition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

public class RDBSequenceServiceTest {

    private final ISequenceService sequenceService = RDBSequenceService.newInstance();

    @AfterEach
    public void cleanup() {
        System.out.println("aslasa");
        new io.sequenceservice.service.db.query.QDSequenceDefinition().findEach(dSequenceDefinition -> {
            sequenceService.deleteSequence(dSequenceDefinition.getId().toString());
        });
    }

    @Test
    public void testCreateAndGet() {
        NumericSequence sequence = sequenceService.createSequence(new NumericSequenceDefinition("ns", "seq1", 5));
        assertSequence(sequence, "ns", "seq1", 5, 5);

        sequence = sequenceService.getSequence("ns", "seq1");
        assertSequence(sequence, "ns", "seq1", 5, 5);
    }

    @Test
    public void testNext() {
        String id = sequenceService.createSequence(new NumericSequenceDefinition("ns2", "seq5", 11)).getId();

        AtomicLong lastValue = new AtomicLong(11);
        LongStream.of(11, 12, 13, 14).forEach(expectedNext -> {
            NumericSequence sequence = sequenceService.getSequence(id);
            assertSequence(sequence, "ns2", "seq5", 11, lastValue.get());
            long nextValue = sequenceService.increment(sequence.getId());
            assertEquals(expectedNext, nextValue, "Incremented value");
            lastValue.set(nextValue);
        });
    }

    @Test
    public void testDelete() {
        NumericSequence sequence = sequenceService.createSequence(new NumericSequenceDefinition("ns3", "seq123", 999));
        assertSequence(sequence, "ns3", "seq123", 999, 999);

        sequenceService.deleteSequence(sequence.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            sequenceService.getSequence(sequence.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            sequenceService.getSequence("ns3", "seq123");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            sequenceService.increment(sequence.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            sequenceService.resetSequence(sequence.getId());
        });
    }

    @Test
    public void testReset() {
        String id = sequenceService.createSequence(new NumericSequenceDefinition("namespaceReset", "seq999", 12345)).getId();
        sequenceService.increment(id);
        sequenceService.increment(id);
        sequenceService.increment(id);
        long currentValue = sequenceService.increment(id);
        assertEquals(12348, currentValue, "after 3 increments");

        sequenceService.resetSequence(id);
        NumericSequence sequence = sequenceService.getSequence(id);
        assertEquals(12345, sequence.getLastValue(), "last value");
        assertEquals(12345, sequenceService.increment(id), "incremented 1st time after reset");
    }

    @Test
    public void testResetWithValue() {
        String id = sequenceService.createSequence(new NumericSequenceDefinition("namespaceReset", "seq999", 12345)).getId();

        sequenceService.resetSequence(id, 98765);
        NumericSequence sequence = sequenceService.getSequence(id);
        assertEquals(98765, sequence.getLastValue(), "last value");
        assertEquals(98765, sequenceService.increment(id), "incremented 1st time");
        assertEquals(98766, sequenceService.increment(id), "incremented 2nd time");
        assertEquals(98767, sequenceService.increment(id), "incremented 3rd time");
    }

    private void assertSequence(NumericSequence sequence, String namespace, String name, long start, long lastValue) {
        assertNotNull(sequence);
        assertEquals(lastValue, sequence.getLastValue(), "last value");

        NumericSequenceDefinition sequenceDefinition = sequence.getSequenceDefinition();
        assertNotNull(sequenceDefinition);
        assertEquals(namespace, sequenceDefinition.getNamespace(), "namespace");
        assertEquals(name, sequenceDefinition.getName(), "name");
        assertEquals(start, sequenceDefinition.getStart(), "start");
    }
}
