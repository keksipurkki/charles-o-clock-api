package net.keksipurkki.charles_o_clock.audit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory
        .getLogger(AuditVerticle.class);

    @Override
    public void start(Promise promise) {
        this.vertx.eventBus().consumer("TECH", this::technicalEvents);
        this.vertx.eventBus().consumer("AUDIT", this::auditEvents);
        promise.complete();
    }

    private void auditEvents(Message<Object> message) {
        System.out.println("Got here!");
        //logger.info("{}", new AuditEvent<>(message));
    }

    private void technicalEvents(Message<Object> message) {
        System.out.println("Got here too!");
        //logger.info("{}", new TechEvent<>(message));
    }

    private static class TechEvent<T> {
        public final String sensitivity = "TECH";
        public final Message<T> message;

        TechEvent(Message<T> message) {
            this.message = message;
        }
    }

    private static class AuditEvent<T> {
        public final String sensitivity = "AUDIT";
        public final Message<T> message;

        AuditEvent(Message<T> message) {
            this.message = message;
        }
    }

}
