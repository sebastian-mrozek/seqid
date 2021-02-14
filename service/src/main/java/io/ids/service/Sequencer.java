package io.ids.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Sequencer {

    Map<UUID, AtomicLong> map = new ConcurrentHashMap<>();

    public void create(UUID id, long start) {
        map.put(id, new AtomicLong(start));
    }

    public long next(UUID id) {
        AtomicLong counter = map.get(id);
        if (counter != null) {
            return counter.getAndIncrement();
        }
        throw new IllegalArgumentException("Sequence with ID: " + id + " does not exist");
    }

    public long getCurrent(UUID id) {
        AtomicLong counter = map.get(id);
        if (counter != null) {
            return counter.get();
        }
        throw new IllegalArgumentException("Sequence with ID: " + id + " does not exist");
    }

    public void reset(UUID id, long start) {
        map.put(id, new AtomicLong(start));
    }

    public void delete(UUID uuid) {
        map.remove(uuid);
    }
}
