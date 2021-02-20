package net.keksipurkki.charles_o_clock.exception;


import com.fasterxml.jackson.annotation.JsonValue;
import io.vertx.core.json.JsonObject;

public abstract class ApiException extends RuntimeException {
    final private int code;

    public int statusCode() {
        return code;
    }

    public ApiException(String message, int status) {
        super(message);
        this.code = status;
    }

    public ApiException(String message, int status, Throwable cause) {
        super(message, cause);
        this.code = status;
    }

    @JsonValue
    private JsonObject toJson() {
        return new JsonObject().put("statusCode", code);
    }

}
