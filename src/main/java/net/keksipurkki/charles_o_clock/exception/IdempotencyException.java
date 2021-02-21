package net.keksipurkki.charles_o_clock.exception;

public class IdempotencyException extends ApiException {
    public IdempotencyException(String message) {
        super(message, 409);
    }
}
