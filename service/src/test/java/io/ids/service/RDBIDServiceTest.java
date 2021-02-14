package io.ids.service;

import io.ids.api.IIDService;
import io.ids.api.NumericSequence;
import io.ids.api.NumericSequenceDefinition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

public class RDBIDServiceTest {

    IIDService idService = RDBIDService.newInstance();

    @AfterEach
    public void cleanup() {
        new io.ids.service.db.query.QDSequenceDefinition().delete();
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
        String id = idService.createSequence(new NumericSequenceDefinition("ns2", "seq5", -1)).getId();

        LongStream.of(-1, 0, 1, 2).forEach(expectedNext -> {
            NumericSequence sequence = idService.getSequence(id);
            long nextValue = idService.increment(sequence.getId());
            assertEquals(expectedNext, nextValue);
            assertSequence(sequence, "ns2", "seq5", -1, expectedNext);
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
            NumericSequence actualSequence = idService.getSequence("ns3", "seq123");
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
        assertEquals(12348, currentValue);

        idService.resetSequence(id);
        NumericSequence sequence = idService.getSequence(id);
        assertEquals(12345, sequence.getNext());
        assertEquals(12345, idService.increment(id));
    }

    @Test
    public void testResetWithValue() {
        String id = idService.createSequence(new NumericSequenceDefinition("namespaceReset", "seq999", 12345)).getId();

        idService.resetSequence(id, 98765);
        NumericSequence sequence = idService.getSequence(id);
        assertEquals(98765, sequence.getNext());
        assertEquals(98765, idService.increment(id));
        assertEquals(98766, idService.increment(id));
        assertEquals(98767, idService.increment(id));
    }

    private void assertSequence(NumericSequence sequence, String namespace, String name, long start, long next) {
        assertNotNull(sequence);
        assertEquals(next, sequence.getNext());

        NumericSequenceDefinition sequenceDefinition = sequence.getSequenceDefinition();
        assertNotNull(sequenceDefinition);
        assertEquals(namespace, sequenceDefinition.getNamespace());
        assertEquals(name, sequenceDefinition.getName());
        assertEquals(start, sequenceDefinition.getStart());
    }
}
