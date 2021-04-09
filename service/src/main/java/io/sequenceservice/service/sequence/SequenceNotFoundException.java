package io.sequenceservice.service.sequence;

public class SequenceNotFoundException extends RuntimeException {

    public SequenceNotFoundException(String id) {
        super("Sequence with id '" + id + "' not found");
    }

    public SequenceNotFoundException(String namespace, String name) {
        super("Sequence '" + namespace + "@" + name + "' not found");
    }
}
