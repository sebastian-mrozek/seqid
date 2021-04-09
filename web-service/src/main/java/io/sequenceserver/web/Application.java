package io.sequenceserver.web;

import io.ebean.DuplicateKeyException;
import io.javalin.Javalin;
import io.sequenceservice.service.sequence.SequenceNotFoundException;

public class Application {

    private final Javalin server;

    public static void main(String[] args) {
        var app = new Application();
        app.start();
    }

    public Application() {
        this.server = Javalin.create();
        WebRoutesRegistration.init(server);
        registerExceptionMappers();
    }

    public void start() {
        server.start();
    }

    public void start(int port) {
        server.start(port);
    }

    public void stop() {
        server.stop();
    }

    private void registerExceptionMappers() {
        server.exception(SequenceNotFoundException.class, ExceptionHandlerFactory.createHandler(SequenceNotFoundException.class, 404));
        server.exception(DuplicateKeyException.class, ExceptionHandlerFactory.createHandler(DuplicateKeyException.class, 400));
    }
}
