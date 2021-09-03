package io.sequenceserver.web;

import io.avaje.http.api.WebRoutes;
import io.avaje.inject.SystemContext;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class WebRoutesRegistration {

    private static final Logger LOG = LoggerFactory.getLogger(WebRoutesRegistration.class);

    public static void init(Javalin server) {
        List<WebRoutes> webRoutes = SystemContext.context().getBeans(WebRoutes.class);
        LOG.info("Found routes: {}", getRouteNames(webRoutes));
        server.routes(() -> webRoutes.forEach(WebRoutes::registerRoutes));
    }

    private static String getRouteNames(List<WebRoutes> webRoutes) {
        return webRoutes.stream()
                .map(route -> route.getClass().getSimpleName())
                .collect(Collectors.joining(","));
    }
}
