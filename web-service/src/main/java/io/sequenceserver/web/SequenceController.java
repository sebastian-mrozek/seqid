package io.sequenceserver.web;

import io.avaje.http.api.*;
import io.sequenceservice.api.ISequenceService;
import io.sequenceservice.api.NumericSequence;
import io.sequenceservice.api.NumericSequenceDefinition;
import io.sequenceservice.service.RDBSequenceService;

import java.util.List;
import java.util.UUID;

@Controller
@Path("sequence")
public class SequenceController {

    private final ISequenceService service;

    public SequenceController() {
        service = RDBSequenceService.newInstance();
        service.createSequence(new NumericSequenceDefinition("ns", "sequence-" + System.currentTimeMillis(), 1));
    }

    @Post
    public NumericSequence createSequence(NumericSequenceDefinition sequenceDefinition) {
        return service.createSequence(sequenceDefinition);
    }

    @Get
    public List<NumericSequence> list(@QueryParam("namespace") String namespace) {
        if (namespace == null || namespace.isEmpty()) {
            return service.listAllSequences();
        } else {
            return service.listSequencesByNamespace(namespace);
        }
    }

    @Get("{id}")
    public NumericSequence getById(UUID id) {
        return service.getSequence(id.toString());
    }

    @Get("{id}/next")
    public long nextById(UUID id) {
        return service.increment(id.toString());
    }

    @Get("{namespace}/{name}")
    public NumericSequence getByName(String namespace, String name) {
        return service.getSequence(namespace, name);
    }

    @Get("{namespace}/{name}/next")
    public long nextByName(String namespace, String name) {
        return service.increment(namespace, name);
    }

    @Patch("{id}")
    public NumericSequence reset(UUID id, NumericSequenceDefinition reset) {
        return service.resetSequence(id.toString(), reset.getStart());
    }

    @Post("{id}")
    public NumericSequence reset(UUID id) {
        return service.resetSequence(id.toString());
    }

    @Delete("{id}")
    public void delete(String id) {
        service.deleteSequence(id);
    }
}
