package net.keksipurkki.charles_o_clock.json;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import net.keksipurkki.charles_o_clock.exception.InvalidInputException;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vertx.ext.web.validation.ValidationHandler.REQUEST_CONTEXT_KEY;

public class JsonMapper<T> {

    private final JsonObject request;
    private final Validator validator;
    private final Class<T> type;

    private JsonMapper(JsonObject request, Class<T> type) {
        this.request = Objects.requireNonNull(request);
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.type = type;
    }

    private T body() {
        var body = request.getJsonObject("body");

        if (body == null) {
            throw new InvalidInputException("Request body not found");
        }

        var pojo = body.mapTo(type);
        var violations = validator.validate(pojo).toArray();

        if (violations.length > 0) {
            throw new InvalidInputException("Request body validation failed", violations);
        }

        return pojo;

    }

    public Optional<UUID> id(String paramName) {
        return Optional.ofNullable(request.getJsonObject("path"))
                       .flatMap(p -> Optional.ofNullable(p.getString(paramName)))
                       .map(UUID::fromString);
    }

    public <R> R map(BiFunction<UUID, T, R> mapper) {
        var uuid = id("uuid").orElseThrow(() -> new InvalidInputException(""));
        return mapper.apply(uuid, body());
    }

    public <R> R map(Function<T, R> mapper) {
        return mapper.apply(body());
    }

    public static <T> JsonMapper<T> parse(RoutingContext ctx, Class<T> type) {
        var request = ctx.<RequestParameters>get(REQUEST_CONTEXT_KEY).toJson();
        return new JsonMapper<>(request, type);
    }

    public static JsonMapper<Void> parse(RoutingContext ctx) {
        return parse(ctx, null);
    }

}
