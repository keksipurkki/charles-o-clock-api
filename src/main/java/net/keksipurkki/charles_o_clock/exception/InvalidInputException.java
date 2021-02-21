package net.keksipurkki.charles_o_clock.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class InvalidInputException extends ApiException {
    public InvalidInputException(String message, Throwable cause) {
        super(message, 400, cause);
    }

    public <T> InvalidInputException(String message, Object... violations) {
        super(message, 400);
    }

    public InvalidInputException(String message) {
        super(message, 400);
    }

}
