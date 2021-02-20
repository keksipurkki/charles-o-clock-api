package net.keksipurkki.charles_o_clock.exception;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}

