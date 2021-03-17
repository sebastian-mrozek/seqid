package io.sequenceserver.web;

import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import io.avaje.http.api.Path;
import io.sequenceservice.api.NumericSequence;
import io.sequenceservice.api.NumericSequenceDefinition;

import java.util.Random;
import java.util.UUID;

@Controller
@Path("sequence")
public class SequenceController {

    @Get
    public NumericSequence random() {
        NumericSequenceDefinition numericSequenceDefinition = new NumericSequenceDefinition("abc", "ns2", 1);
        return new NumericSequence(UUID.randomUUID().toString(), numericSequenceDefinition, 2);
    }
}
