package io.ids.service;

import io.ids.api.IIDService;
import io.ids.api.NumericSequence;
import io.ids.api.NumericSequenceDefinition;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

public class RDBIDServiceTest {

    private final IIDService idService = RDBIDService.newInstance();

    @After
    public void cleanup() {
        new io.ids.service.db.query.QDSequenceDefinition().findEach(dSequenceDefinition -> {
            idService.deleteSequence(dSequenceDefinition.getId().toString());
        });
    }

    @Test
    public void testCreateAndGet() {
        NumericSequence sequence = idService.createSequence(new NumericSequenceDefinition("ns", "seq1", 5));
        assertSequence(sequence, "ns", "seq1", 5, 5);

        sequence = idService.getSequence("ns", "seq1");
        assertSequence(sequence, "ns", "seq1", 5, 5);
    }

    @Test
    public void testNext() {
        String id = idService.createSequence(new NumericSequenceDefinition("ns2", "seq5", 11)).getId();

        AtomicLong lastValue = new AtomicLong(11);
        LongStream.of(11, 12, 13, 14).forEach(expectedNext -> {
            NumericSequence sequence = idService.getSequence(id);
            assertSequence(sequence, "ns2", "seq5", 11, lastValue.get());
            long nextValue = idService.increment(sequence.getId());
            assertEquals(expectedNext, nextValue, "Incremented value");
            lastValue.set(nextValue);
        });
    }

    @Test
    public void testDelete() {
        NumericSequence sequence = idService.createSequence(new NumericSequenceDefinition("ns3", "seq123", 999));
        assertSequence(sequence, "ns3", "seq123", 999, 999);

        idService.deleteSequence(sequence.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            idService.getSequence(sequence.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            idService.getSequence("ns3", "seq123");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            idService.increment(sequence.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            idService.resetSequence(sequence.getId());
        });
    }

    @Test
    public void testReset() {
        String id = idService.createSequence(new NumericSequenceDefinition("namespaceReset", "seq999", 12345)).getId();
        idService.increment(id);
        idService.increment(id);
        idService.increment(id);
        long currentValue = idService.increment(id);
        assertEquals(12348, currentValue, "after 3 increments");

        idService.resetSequence(id);
        NumericSequence sequence = idService.getSequence(id);
        assertEquals(12345, sequence.getLastValue(), "last value");
        assertEquals(12345, idService.increment(id), "incremented 1st time after reset");
    }

    @Test
    public void testResetWithValue() {
        String id = idService.createSequence(new NumericSequenceDefinition("namespaceReset", "seq999", 12345)).getId();

        idService.resetSequence(id, 98765);
        NumericSequence sequence = idService.getSequence(id);
        assertEquals(98765, sequence.getLastValue(), "last value");
        assertEquals(98765, idService.increment(id), "incremented 1st time");
        assertEquals(98766, idService.increment(id), "incremented 2nd time");
        assertEquals(98767, idService.increment(id), "incremented 3rd time");
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
