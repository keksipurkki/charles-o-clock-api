package net.keksipurkki.charles_o_clock.api;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import net.keksipurkki.charles_o_clock.domain.*;
import net.keksipurkki.charles_o_clock.json.JsonMapper;

import java.util.UUID;

public enum Api implements CharlesOClock {

    /* API Operations */
    REGISTER,
    GET_USER_BY_ID,
    ADD_TAG;

    /* Implementations */
    @Override
    public Future<User> register(PhoneNumber phoneNumber) {
        return null;
    }

    @Override
    public Future<User> getById(UUID id) {
        return null;
    }

    @Override
    public Future<Tag[]> addTag(User user, Tag tag) {
        return null;
    }

    /* Vert.x integration */
    public static Handler<RoutingContext> create(String operationId) {
        final var api = Api.valueOf(operationId);
        return rc -> api.handle(rc).flatMap(rc::json);
    }

    public static void error(RoutingContext ctx) {
        ctx.response().setStatusCode(500).end();
    }

    private Future<?> handle(RoutingContext rc) {
        return switch (this) {
            case REGISTER -> JsonMapper.parse(rc, PhoneNumber.class).map(this::register);
            case GET_USER_BY_ID -> JsonMapper.parse(rc).id("uuid").map(this::getById).orElse(Future.failedFuture("sdgs"));
            case ADD_TAG -> JsonMapper.parse(rc, Tag.class).map((id, body) -> this.getById(id).flatMap(user -> this.addTag(user, body)));
        };
    }

}
