package io.sequenceserver.web;

import io.avaje.http.api.*;
import io.sequenceservice.api.ISequenceService;
import io.sequenceservice.api.NumericSequence;
import io.sequenceservice.api.NumericSequenceDefinition;
import io.sequenceservice.service.RDBSequenceService;

import java.util.List;

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

    @Get("id/{id}")
    public NumericSequence getById(String id) {
        return service.getSequence(id);
    }

    @Get("id/{id}/next")
    public long next(String id) {
        return service.increment(id);
    }

    @Get("name/{namespace}/{name}")
    public NumericSequence byName(String namespace, String name) {
        return service.getSequence(namespace, name);
    }

    @Get("name/{namespace}/{name}/next")
    public long next(String namespace, String name) {
        return service.increment(namespace, name);
    }

    @Get
    public List<NumericSequence> list(@QueryParam("namespace") String namespace) {
        if (namespace == null || namespace.isEmpty()) {
            return service.listAllSequences();
        } else {
            return service.listSequencesByNamespace(namespace);
        }
    }


}
