package net.keksipurkki.charles_o_clock.exception;

public class ConflictingStateException extends RuntimeException {
    public ConflictingStateException(String message) {
        super(message);
    }
}
