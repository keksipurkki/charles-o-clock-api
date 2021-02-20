package net.keksipurkki.charles_o_clock;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import net.keksipurkki.charles_o_clock.audit.AuditVerticle;
import net.keksipurkki.charles_o_clock.http.HttpVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {

        logger.info("Starting {}", Main.class.getName());

        var vertx = Vertx.vertx();
        var httpVerticle = new HttpVerticle();
        var auditVerticle = new AuditVerticle();

        Future.succeededFuture()
              .compose(v -> deploy(vertx, httpVerticle))
              .compose(v -> deploy(vertx, auditVerticle))
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
