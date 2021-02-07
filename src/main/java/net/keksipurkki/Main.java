package net.keksipurkki;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import net.keksipurkki.verticle.EventVerticle;
import net.keksipurkki.verticle.HttpVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.core.CompositeFuture.all;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String... args) {

    logger.info("Starting {}", Main.class.getName());

    var vertx = Vertx.vertx();
    var httpVerticle = new HttpVerticle();
    var eventVerticle = new EventVerticle();

    all(deploy(vertx, httpVerticle), deploy(vertx, eventVerticle))
      .onComplete(deployment -> {

        if (deployment.failed()) {
          logger.error("Aborting", deployment.cause());
          vertx.close();
          System.exit(1);
        }

        if (deployment.succeeded()) {
          logger.info("Deployment completed. App is now running.");
        }
    });
  }

  private static Future<String> deploy(Vertx vertx, AbstractVerticle verticle) {
    return Future.future(promise -> {
      vertx.deployVerticle(verticle, result -> {
        if (result.succeeded()) promise.complete(result.result());
        if (result.failed()) promise.fail(result.cause());
      });
    });
  }
}
