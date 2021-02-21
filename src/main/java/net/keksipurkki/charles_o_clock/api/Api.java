package net.keksipurkki.charles_o_clock.api;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import net.keksipurkki.charles_o_clock.domain.PhoneNumber;
import net.keksipurkki.charles_o_clock.domain.Tag;
import net.keksipurkki.charles_o_clock.domain.User;
import net.keksipurkki.charles_o_clock.exception.ApiException;
import net.keksipurkki.charles_o_clock.exception.IdempotencyException;
import net.keksipurkki.charles_o_clock.exception.NotFoundException;
import net.keksipurkki.charles_o_clock.json.JsonMapper;
import net.keksipurkki.charles_o_clock.repository.Repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;

public enum Api implements CharlesOClock {

    /* API Operations */
    REGISTER,
    GET_USER_BY_ID,
    ADD_TAG;

    private static final Logger logger = LoggerFactory.getLogger(Api.class);

    /* Implementations */
    @Override
    public Future<User> register(PhoneNumber phoneNumber) {
        return Repositories.users.createFrom(phoneNumber);
    }

    @Override
    public Future<User> getById(UUID id) {
        return Repositories.users.getById(id).map(user -> {
            logger.debug("User found: "+user.isPresent());
            if (user.isEmpty()) throw new NotFoundException("No user with id "+id);
            return user.get();
        });
    }

    @Override
    public Future<Tag[]> addTag(User user, Tag tag) {
        return Repositories.tags.getByUser(user).flatMap(tags -> {
            if (Set.of(tags).contains(tag)) throw new IdempotencyException("Tag already exists");
            return Repositories.tags.put(user, tag);
        });
    }

    /* Vert.x integration */
    public static Handler<RoutingContext> create(String operationId) {
        final var api = Api.valueOf(operationId);
        return rc -> api.handle(rc).flatMap(rc::json).onFailure(rc::fail);
    }

    public static void error(RoutingContext ctx) {
        logger.debug("Running API error handler");
        var failure = ctx.failure();
        failure.printStackTrace();

        if (failure instanceof ApiException error) {
            ctx.response()
               .setStatusCode(error.statusCode())
               .putHeader("Content-Type", "application/json")
               .end(JsonMapper.encode(error));
        } else {
            logger.warn("Unexpected request failure {}", failure.getClass().getName());
            ctx.response()
               .setStatusCode(500)
               .end(failure.getMessage());
        }
    }

    private Future<?> handle(RoutingContext rc) {
        logger.info("Handling "+this);
        return switch (this) {
            case REGISTER -> JsonMapper.parse(rc, PhoneNumber.class).map(this::register);
            case GET_USER_BY_ID -> JsonMapper.parse(rc).id("uuid").map(this::getById).orElse(Future.failedFuture("sdgs"));
            case ADD_TAG -> JsonMapper.parse(rc, Tag.class).map((id, body) -> this.getById(id).flatMap(user -> this.addTag(user, body)));
        };
    }

}
