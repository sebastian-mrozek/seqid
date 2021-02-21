package io.sequenceservice.service;

import io.ebean.DB;
import io.ebean.annotation.Platform;
import io.ebean.annotation.Transactional;
import io.sequenceservice.api.ISequenceService;
import io.sequenceservice.api.NumericSequence;
import io.sequenceservice.api.NumericSequenceDefinition;
import io.sequenceservice.service.sequence.ISequencer;
import io.sequenceservice.service.db.SequenceMapper;
import io.sequenceservice.service.sequence.SequencerFactory;
import io.sequenceservice.service.db.DSequenceDefinition;
import io.sequenceservice.service.db.query.QDSequenceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;


public class RDBSequenceService implements ISequenceService {

    public static final Logger LOG = LoggerFactory.getLogger(RDBSequenceService.class);

    private final SequenceMapper mapper;
    private final ISequencer sequencer;

    private RDBSequenceService(SequenceMapper mapper, ISequencer sequencer) {
        this.mapper = mapper;
        this.sequencer = sequencer;
    }

    public static ISequenceService newInstance() {
        Platform platform = DB.getDefault().getPlatform();
        ISequencer sequencer = SequencerFactory.forPlatform(platform);
        return new RDBSequenceService(new SequenceMapper(), sequencer);
    }

    @Transactional
    @Override
    public NumericSequence createSequence(NumericSequenceDefinition sequenceDefinition) {
        DSequenceDefinition dbSequence = mapper.toDb(sequenceDefinition);
        dbSequence.save();
        sequencer.create(dbSequence.getId(), dbSequence.getStart());
        LOG.info("Created new sequence: {}", sequenceDefinition);
        return mapper.toApi(dbSequence, dbSequence.getStart());
    }

    @Override
    public NumericSequence getSequence(String namespace, String name) {
        DSequenceDefinition dbSequence = findSequenceOrThrow(namespace, name);
        NumericSequence numericSequence = mapper.toApi(dbSequence, sequencer.getCurrent(dbSequence.getId()));
        LOG.debug("Retrieved sequence by name '{}@{}: {}", name, namespace, numericSequence);
        return numericSequence;
    }

    @Transactional
    @Override
    public NumericSequence resetSequence(String id) {
        DSequenceDefinition dSequence = findSequenceOrThrow(id);
        sequencer.reset(dSequence.getId(), dSequence.getStart());
        NumericSequence numericSequence = mapper.toApi(dSequence, sequencer.getCurrent(dSequence.getId()));
        LOG.info("Sequence id: '{}' reset: {}", id, numericSequence);
        return numericSequence;
    }

    @Transactional
    @Override
    public NumericSequence resetSequence(String id, long start) {
        DSequenceDefinition dSequence = findSequenceOrThrow(id);
        sequencer.reset(dSequence.getId(), start);
        NumericSequence numericSequence = mapper.toApi(dSequence, sequencer.getCurrent(dSequence.getId()));
        LOG.info("Sequence id: '{}' reset: {}", id, numericSequence);
        return numericSequence;
    }

    @Transactional
    @Override
    public void deleteSequence(String id) {
        UUID uuid = UUID.fromString(id);
        new QDSequenceDefinition()
                .id.eq(uuid)
                .delete();
        sequencer.delete(uuid);
        LOG.info("Sequence id: '{}' deleted", id);
    }

    @Override
    public NumericSequence getSequence(String id) {
        DSequenceDefinition dbSequence = findSequenceOrThrow(id);
        NumericSequence numericSequence = mapper.toApi(dbSequence, sequencer.getCurrent(dbSequence.getId()));
        LOG.debug("Retrieved sequence by id: '{}': {}", id, numericSequence);
        return numericSequence;
    }

    @Override
    public long increment(String namespace, String name) {
        NumericSequence sequence = getSequence(namespace, name);
        long value = increment(sequence.getId());
        LOG.trace("Sequence '{}@{}' incremented to: {}", name, namespace, value);
        return value;
    }

    @Override
    public long increment(String id) {
        return sequencer.next(UUID.fromString(id));
    }

    private DSequenceDefinition findSequenceOrThrow(String id) {
        UUID sequenceId = UUID.fromString(id);
        Optional<DSequenceDefinition> dbSequenceResult = new QDSequenceDefinition()
                .id.eq(sequenceId)
                .findOneOrEmpty();
        if (dbSequenceResult.isEmpty()) {
            throw new IllegalArgumentException("Sequence of id " + id + " does not exist");
        }
        return dbSequenceResult.get();
    }

    private DSequenceDefinition findSequenceOrThrow(String namespace, String name) {
        Optional<DSequenceDefinition> dbSequenceResult = new QDSequenceDefinition()
                .namespace.eq(namespace)
                .name.eq(name)
                .findOneOrEmpty();

        if (dbSequenceResult.isEmpty()) {
            throw new IllegalArgumentException("Sequence " + name + " in namespace " + namespace + " does not exist");
        }

        return dbSequenceResult.get();
    }

}
