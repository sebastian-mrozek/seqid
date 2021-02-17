package io.sequenceservice.service.sequence;

import java.util.UUID;

public interface ISequencer {
    void create(UUID id, long start);

    long next(UUID id);

    long getCurrent(UUID id);

    void reset(UUID id, long start);

    void delete(UUID uuid);
}
