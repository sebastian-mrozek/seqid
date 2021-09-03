package io.sequenceserver.web;

import io.avaje.inject.Bean;
import io.avaje.inject.Factory;
import io.sequenceservice.api.ISequenceService;
import io.sequenceservice.service.RDBSequenceService;

@Factory
public class SequenceServiceFactory {

    @Bean
    public ISequenceService get() {
        return RDBSequenceService.newInstance();
    }
}
