package net.keksipurkki.charles_o_clock.api;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameter;
import io.vertx.ext.web.validation.RequestParameters;
import net.keksipurkki.charles_o_clock.domain.*;
import net.keksipurkki.charles_o_clock.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.UUID;

import static io.vertx.ext.web.validation.ValidationHandler.REQUEST_CONTEXT_KEY;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public enum Api implements CharlesOClock {

    /* API Operations */
    CREATE_TAG,
    GET_CLIENT,
    REGISTER_CLIENT,
    CLAIM_TAG,
    DISCLAIM_TAG,
    CLOCK;

    private static final Logger logger = LoggerFactory.getLogger(Api.class);
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public Future<Tag> createTag() {
        return TagRepository.shared.createTag(UUID.randomUUID());
    }

    @Override
    public Future<ClientCredentials> register(PhoneNumber number) {
        return ClientRepository.shared.getByPhoneNumber(number).compose(existing -> {

            if (existing.isPresent()) {
                throw new RegistrationException("Client already registered");
            }

            var client = ClientRepository.shared.create(number);
            return ClientRepository.shared.put(client);

        }).map(client -> {
                return new ClientCredentials(client.id(), client.keyPair().getPrivate());
            }
        );
    }

    @Override
    public Future<Client> getById(UUID id) {
        System.out.println("xyz");
        throw new NotFoundException("No client with id "+id);
    }

    @Override
    public Future<Tag> claim(Client client, Tag tag) {
        return TagRepository.shared.getById(tag.id()).compose(existing -> {

            if (existing.isEmpty()) {
                var message = String.format("No tag with id %s", tag.id());
                throw new NotFoundException(message);
            }

            var target = existing.get();

            if (nonNull(target.client())) {
                var message = String.format("Tag %s already owned by client %s", tag.id(), client.id());
                throw new TagClaimException(message);
            }

            return TagRepository.shared.claim(client, tag);

        });
    }

    @Override
    public Future<Tag> disclaim(Client client, Tag tag) {
        return TagRepository.shared.getById(tag.id()).compose(existing -> {

            if (existing.isEmpty()) {
                var message = String.format("No tag with id %s", tag.id());
                throw new NotFoundException(message);
            }

            var target = existing.get();

            if (isNull(target.client()) || !target.client()
                                                     .equals(client)) {
                var message = String.format("Tag %s has not been claimed by client %s", tag.id(), client.id());
                throw new TagClaimException(message);
            }

            return TagRepository.shared.disclaim(client, tag);

        });

    }

    @Override
    public Future<ClockStatus> clock(ClockStatus status) {
        final var client = status.client();
        return ClockStateRepository.shared.getLatest(client).compose(latest -> {

            if (latest.isEmpty()) {
                if (status.action().equals(Action.CLOCK_OUT)) {
                    var message = String
                        .format("No existing clock state for client %s", client);
                    throw new ConflictingStateException(message);
                }
            }

            if (latest.isPresent() && latest.get().action().equals(status.action())) {
                var message = String.format("Already in state %s", status.action());
                throw new ConflictingStateException(message);
            }

            return ClockStateRepository.shared.put(status);

        });
    }

    /* Vert.x integration */

    public static void handle(RoutingContext ctx) {
        final var operation = ctx.<JsonObject>get("operation");
        final var params = ctx.<RequestParameters>get(REQUEST_CONTEXT_KEY);
        final var body = Optional.ofNullable(params.body())
                                            .map(RequestParameter::getJsonObject)
                                            .orElseGet(JsonObject::new);

        var api = Api.valueOf(operation.getString("operationId"));
        var future = switch(api) {
            case CREATE_TAG -> api.createTag();
            case GET_CLIENT -> api.getById(null);
            case REGISTER_CLIENT -> api.register(body.mapTo(PhoneNumber.class));
            case CLAIM_TAG -> api.claim(null, null);
            case DISCLAIM_TAG -> api.claim(null, null);
            case CLOCK -> api.clock(null);
        };
        future.compose(ctx::json);
    }

    public static void error(RoutingContext ctx) {

        logger.debug("Running API error handler");
        var failure = ctx.failure();
        failure.printStackTrace();

        if (failure instanceof ApiException) {
            var error = (ApiException) failure;
            ctx.response()
               .setStatusCode(error.statusCode())
               .putHeader("Content-Type", "application/json")
               .end(Json.encode(error));
        } else {
            ctx.response()
               .setStatusCode(500)
               .end(failure.getMessage());
        }
    }
}
