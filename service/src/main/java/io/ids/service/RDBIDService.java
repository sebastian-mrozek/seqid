package io.ids.service;

import io.ebean.annotation.Transactional;
import io.ids.api.IIDService;
import io.ids.api.NumericSequence;
import io.ids.api.NumericSequenceDefinition;
import io.ids.service.db.DSequenceDefinition;
import io.ids.service.db.query.QDSequenceDefinition;

import java.util.Optional;
import java.util.UUID;


public class RDBIDService implements IIDService {

    private final SequenceMapper mapper;
    private final Sequencer sequencer;

    public static IIDService newInstance() {
        return new RDBIDService(new SequenceMapper(), new Sequencer());
    }

    public RDBIDService(SequenceMapper mapper, Sequencer sequencer) {
        this.mapper = mapper;
        this.sequencer = sequencer;
    }

    @Transactional
    @Override
    public NumericSequence createSequence(NumericSequenceDefinition sequenceDefinition) {
        DSequenceDefinition dbSequence = mapper.toDb(sequenceDefinition);
        dbSequence.save();
        sequencer.create(dbSequence.getId(), dbSequence.getStart());
        return mapper.toApi(dbSequence, dbSequence.getStart());
    }

    @Override
    public NumericSequence getSequence(String namespace, String name) {
        DSequenceDefinition dbSequence = findSequenceOrThrow(namespace, name);
        return mapper.toApi(dbSequence, sequencer.getCurrent(dbSequence.getId()));
    }

    @Transactional
    @Override
    public NumericSequence resetSequence(String id) {
        DSequenceDefinition dSequence = findSequenceOrThrow(id);
        sequencer.reset(dSequence.getId(), dSequence.getStart());
        return mapper.toApi(dSequence, sequencer.getCurrent(dSequence.getId()));
    }

    @Transactional
    @Override
    public NumericSequence resetSequence(String id, long start) {
        DSequenceDefinition dSequence = findSequenceOrThrow(id);
        sequencer.reset(dSequence.getId(), start);
        return mapper.toApi(dSequence, sequencer.getCurrent(dSequence.getId()));
    }

    @Transactional
    @Override
    public void deleteSequence(String id) {
        UUID uuid = UUID.fromString(id);
        new QDSequenceDefinition()
                .id.eq(uuid)
                .delete();
        sequencer.delete(uuid);
    }

    @Override
    public NumericSequence getSequence(String id) {
        DSequenceDefinition dbSequence = findSequenceOrThrow(id);
        return mapper.toApi(dbSequence, sequencer.getCurrent(dbSequence.getId()));
    }

    @Override
    public long increment(String namespace, String name) {
        NumericSequence sequence = getSequence(namespace, name);
        return increment(sequence.getId());
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
