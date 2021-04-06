package io.sequenceserver.web;

import io.avaje.http.api.*;
import io.javalin.http.Context;
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

    @Get
    public List<NumericSequence> list(@QueryParam("namespace") String namespace) {
        if (namespace == null || namespace.isEmpty()) {
            return service.listAllSequences();
        } else {
            return service.listSequencesByNamespace(namespace);
        }
    }

    @Get("{id}")
    public NumericSequence getById(String id) {
        return service.getSequence(id);
    }

    @Get("{id}/next")
    public long nextById(String id) {
        return service.increment(id);
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
    public NumericSequence reset(String id, Context context) {
        String body = context.body();
        if (body.isBlank()) {
            return service.resetSequence(id);
        } else {
            NumericSequenceDefinition reset = context.bodyAsClass(NumericSequenceDefinition.class);
            return service.resetSequence(id, reset.getStart());
        }
    }

    @Delete("{id}")
    public void delete(String id) {
        service.deleteSequence(id);
    }
}
