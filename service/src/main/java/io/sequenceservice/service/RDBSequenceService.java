package io.sequenceservice.service;

import io.ebean.DB;
import io.ebean.annotation.Platform;
import io.ebean.annotation.Transactional;
import io.sequenceservice.api.ISequenceService;
import io.sequenceservice.api.NumericSequence;
import io.sequenceservice.api.NumericSequenceDefinition;
import io.sequenceservice.service.db.DSequenceDefinition;
import io.sequenceservice.service.db.SequenceMapper;
import io.sequenceservice.service.db.query.QDSequenceDefinition;
import io.sequenceservice.service.sequence.ISequencer;
import io.sequenceservice.service.sequence.SequenceNotFoundException;
import io.sequenceservice.service.sequence.SequencerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        long current = sequencer.getCurrent(dbSequence.getId());
        return mapper.toApi(dbSequence, toSequenceString(dbSequence, current));
    }

    @Override
    public NumericSequence getSequence(String namespace, String name) {
        DSequenceDefinition dbSequence = findSequenceOrThrow(namespace, name);
        long current = sequencer.getCurrent(dbSequence.getId());
        NumericSequence numericSequence = mapper.toApi(dbSequence, toSequenceString(dbSequence, current));
        LOG.debug("Retrieved sequence by name '{}@{}: {}", name, namespace, numericSequence);
        return numericSequence;
    }

    @Transactional
    @Override
    public NumericSequence resetSequence(String id) {
        DSequenceDefinition dbSequence = findSequenceOrThrow(id);
        sequencer.reset(dbSequence.getId(), dbSequence.getStart());
        long current = sequencer.getCurrent(dbSequence.getId());
        NumericSequence numericSequence = mapper.toApi(dbSequence, toSequenceString(dbSequence, current));
        LOG.info("Sequence id: '{}' reset: {}", id, numericSequence);
        return numericSequence;
    }

    @Transactional
    @Override
    public NumericSequence resetSequence(String id, long start) {
        DSequenceDefinition dbSequence = findSequenceOrThrow(id);
        dbSequence.setStart(start);
        dbSequence.update();
        sequencer.reset(dbSequence.getId(), start);
        long current = sequencer.getCurrent(dbSequence.getId());
        NumericSequence numericSequence = mapper.toApi(dbSequence, toSequenceString(dbSequence, current));
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
        long current = sequencer.getCurrent(dbSequence.getId());
        NumericSequence numericSequence = mapper.toApi(dbSequence, toSequenceString(dbSequence, current));
        LOG.debug("Retrieved sequence by id: '{}': {}", id, numericSequence);
        return numericSequence;
    }

    @Override
    public String increment(String namespace, String name) {
        DSequenceDefinition dbSequence = findSequenceOrThrow(namespace, name);
        long value = sequencer.next(dbSequence.getId());
        LOG.trace("Sequence '{}@{}' incremented to: {}", name, namespace, value);
        return toSequenceString(dbSequence, value);
    }

    @Override
    public String increment(String id) {
        DSequenceDefinition dbSequence = findSequenceOrThrow(id);
        long value = sequencer.next(UUID.fromString(id));
        LOG.trace("Sequence id: '{}' incremented to: {}", id, value);
        return toSequenceString(dbSequence, value);
    }

    @Override
    public List<NumericSequenceDefinition> listAllDefinitions() {
        return new QDSequenceDefinition()
                .findList().stream()
                .map(mapper::toApi)
                .collect(Collectors.toList());
    }

    @Override
    public List<NumericSequenceDefinition> listDefinitionsByNamespace(String namespace) {
        return new QDSequenceDefinition()
                .namespace.eq(namespace)
                .findList().stream()
                .map(mapper::toApi)
                .collect(Collectors.toList());
    }

    @Override
    public List<NumericSequence> listAllSequences() {
        return new QDSequenceDefinition()
                .findList().stream()
                .map(this::toFullSequenceDetails)
                .collect(Collectors.toList());
    }

    @Override
    public List<NumericSequence> listSequencesByNamespace(String namespace) {
        return new QDSequenceDefinition()
                .namespace.eq(namespace)
                .findList().stream()
                .map(this::toFullSequenceDetails)
                .collect(Collectors.toList());
    }

    private NumericSequence toFullSequenceDetails(DSequenceDefinition dSequenceDefinition) {
        long current = this.sequencer.getCurrent(dSequenceDefinition.getId());
        return mapper.toApi(dSequenceDefinition, toSequenceString(dSequenceDefinition, current));
    }

    private DSequenceDefinition findSequenceOrThrow(String id) {
        UUID sequenceId = UUID.fromString(id);
        Optional<DSequenceDefinition> dbSequenceResult = new QDSequenceDefinition()
                .id.eq(sequenceId)
                .findOneOrEmpty();
        if (dbSequenceResult.isEmpty()) {
            throw new SequenceNotFoundException(id);
        }
        return dbSequenceResult.get();
    }

    private DSequenceDefinition findSequenceOrThrow(String namespace, String name) {
        Optional<DSequenceDefinition> dbSequenceResult = new QDSequenceDefinition()
                .namespace.eq(namespace)
                .name.eq(name)
                .findOneOrEmpty();

        if (dbSequenceResult.isEmpty()) {
            throw new SequenceNotFoundException(namespace, name);
        }

        return dbSequenceResult.get();
    }

    private String toSequenceString(DSequenceDefinition definition, long value) {
        if (value < definition.getStart()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        if (definition.getPrefix() != null) {
            builder.append(definition.getPrefix());
        }

        if (definition.getPadding() != null) {
            String valueString = String.format("%" + definition.getPadding() + "d", value).replace(" ", "0");
            builder.append(valueString);
        } else {
            builder.append(value);
        }

        if (definition.getSuffix() != null) {
            builder.append(definition.getSuffix());
        }

        return builder.toString();

    }
}
