package net.keksipurkki.charles_o_clock.http;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.validation.BadRequestException;
import net.keksipurkki.charles_o_clock.exception.InvalidInputException;
import net.keksipurkki.charles_o_clock.exception.NotFoundException;
import net.keksipurkki.charles_o_clock.exception.UnsafeInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.nonNull;

public class Middlewares {

    private static final Logger logger = LoggerFactory
        .getLogger(Middlewares.class);

    public static BodyHandler bodyHandler() {
        return BodyHandler.create().setBodyLimit(4096L);
    }

    public static Handler<RoutingContext> routeNotFound() {
        return rc -> {
            final var message = "No route for "+rc.request().uri();
            rc.fail(new NotFoundException(message));
            rc.next();
        };
    }

    public static Handler<RoutingContext> defaultHeaders() {
        return rc -> {
            rc.response()
              .putHeader("Content-Type", "application/json; charset=utf-8")
              .putHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate")
              .putHeader("Pragma", "no-cache")
              .putHeader("Expires", "0");
            rc.next();
        };
    }

    /* Translate exceptions to domain model */
    public static Handler<RoutingContext> errorHandler() {
        return rc -> {

            logger.debug("Running error handler middleware");

            if (nonNull(rc.failure())) {
                logger.debug("Failure is {}", rc.failure().getClass().getName());
            }

            // Request body is too big
            if (rc.statusCode() == 413) {
                rc.fail(new UnsafeInputException("Input is unsafe to process", rc.statusCode()));
                return;
            }

            // Exceptions thrown by Vert.x validation service
            if (rc.failure() instanceof BadRequestException) {
                rc.fail(new InvalidInputException("Bad request", rc.failure()));
                return;
            }

            rc.next();

        };
    }

    // TODO
    public static Handler<RoutingContext> requestLogging() {
        return RoutingContext::next;
    }

}
