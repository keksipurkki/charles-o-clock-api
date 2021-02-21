package net.keksipurkki.charles_o_clock.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.Operation;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import net.keksipurkki.charles_o_clock.api.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpVerticle.class);

    @Override
    public void start(Promise<Void> promise) {
        logger.info("Starting {}", HttpVerticle.class.getName());

        var url = openApiSpecification();
        logger.debug("Reading OpenAPI specification from {}", url);

        RouterBuilder.create(vertx, url)
                     .map(this::createRouter)
                     .map(this::createServer)
                     .onComplete(done(promise));
    }

    private String openApiSpecification() {
        return HttpVerticle.class.getResource("/api.yaml").toString();
    }

    private RouterBuilderOptions getOptions() {
        return new RouterBuilderOptions()
            .setOperationModelKey("operation");
    }

    private Router createRouter(RouterBuilder builder) {

        builder
            .setOptions(getOptions())
            .bodyHandler(Middlewares.bodyHandler())
            .rootHandler(Middlewares.defaultHeaders())
            .rootHandler(Middlewares.requestLogging());

        // Resources from API specification
        for (var operation : builder.operations()) {
            route(builder, operation);
        }

        var router = builder.createRouter();

        // Not Found otherwise
        router.route().handler(Middlewares.routeNotFound());

        // Global error handler (actual status code is determined by Api::error)
        router.errorHandler(500, Api::error);

        return router;

    }

    private Operation route(RouterBuilder builder, Operation operation) {
        var operationId = operation.getOperationId();
        var path = operation.getOpenAPIPath();
        logger.debug("Mounting {} ({})", operationId, path);
        return builder.operation(operationId)
                      .handler(Api.create(operationId))
                      .failureHandler(Middlewares.errorHandler());
    }

    private HttpServer createServer(Router router) {
        logger.debug("Creating a HTTP server instance");
        return vertx.createHttpServer().requestHandler(router);
    }

    private Handler<AsyncResult<HttpServer>> done(Promise<Void> promise) {
        return (result) -> {
            if (result.failed()) {
                promise.fail(result.cause());
            } else {
                var server = result.result();
                server.listen(8080);
                promise.complete();
            }
        };
    }

}
