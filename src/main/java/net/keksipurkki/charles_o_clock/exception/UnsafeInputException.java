package net.keksipurkki.charles_o_clock.exception;

public class UnsafeInputException extends ApiException {
    public UnsafeInputException(String message, int status) {
        super(message, status);
    }
}
