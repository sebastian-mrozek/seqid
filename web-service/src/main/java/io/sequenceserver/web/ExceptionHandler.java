package io.sequenceserver.web;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class ExceptionHandler implements io.javalin.http.ExceptionHandler<IllegalArgumentException> {

    @Override
    public void handle(@NotNull IllegalArgumentException exception, @NotNull Context ctx) {
        exception.printStackTrace();
        ctx.status(404);
    }
}
