package io.sequenceservice.service.sequence;

import io.ebean.annotation.Platform;

public class SequencerFactory {

    public static ISequencer forPlatform(Platform platform) {
        SequenceQueryProvider queryProvider = SequenceQueryProvider.forPlatform(platform);
        switch (platform) {
            case POSTGRES:
                return new PostgresSequencer(queryProvider);
            case H2:
                return new H2Sequencer(queryProvider);
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

}
