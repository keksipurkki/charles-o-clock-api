package net.keksipurkki.charles_o_clock.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import io.vertx.core.json.JsonObject;

public class InvalidInputException extends ApiException {

    private Object[] violations = {};

    public InvalidInputException(String message, Throwable cause) {
        super(message, 400, cause);
    }

    public <T> InvalidInputException(String message, Object... violations) {
        super(message, 400);
        this.violations = violations;
    }

    public InvalidInputException(String message) {
        super(message, 400);
    }

    @Override
    @JsonValue
    protected JsonObject toJson() {
        return super.toJson().put("violations", this.violations);
    }

}
