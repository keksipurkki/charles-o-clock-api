package net.keksipurkki.charles_o_clock.exception;

public class InvalidInputException extends ApiException {
    public InvalidInputException(String message, Throwable cause) {
        super(message, 400, cause);
    }

    public InvalidInputException(String message) {
        super(message, 400);
    }

}
