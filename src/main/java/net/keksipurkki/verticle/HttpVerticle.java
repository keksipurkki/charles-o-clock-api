package net.keksipurkki.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import net.keksipurkki.clock.Api;
import net.keksipurkki.diagnostics.DiagnosticsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

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
      .onComplete(this.done(promise));
  }

  private String openApiSpecification() {
    return HttpVerticle.class.getResource("/api.yaml").toString();
  }

  private Router createRouter(RouterBuilder builder) {
    var api = Api.create();

    var options = new RouterBuilderOptions();
    options.setOperationModelKey("operation");
    builder.setOptions(options);

    // Resources from API specification
    for (var operation: builder.operations()) {

      var operationId = operation.getOperationId();
      var path = operation.getOpenAPIPath();

      logger.debug("Mounting {} ({})", operationId, path);
      builder.operation(operationId)
        .handler(api::handle)
        .failureHandler(api::error);
    }

    var router = builder.createRouter();

    // Undocumented diagnostics endpoint
    router.get("/_diagnostics").handler(new DiagnosticsHandler());

    // Not Found otherwise
    router.route().handler(ctx -> ctx.response().setStatusCode(404).end());

    return router;

  }

  private Handler<RoutingContext> envelope(Supplier<Object> operation) {
    return ctx -> {
      ctx.response().setStatusCode(200).end(Json.encode(operation.get()));
    };
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
        logger.info("Server listening on {}", server.actualPort());
        promise.complete();
      }
    };
  }

}
