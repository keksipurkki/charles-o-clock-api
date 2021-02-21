package net.keksipurkki.charles_o_clock.domain;

import net.keksipurkki.charles_o_clock.exception.InvalidInputException;

import java.util.Optional;
import java.util.function.Predicate;

public class Invariants {

    public static Predicate<String> isNotBlank = (s) -> !Optional.ofNullable(s).orElse("").isBlank();

    public static <T> T invariant(Predicate<T> predicate, T input) {
        if (!predicate.test(input)) throw new InvalidInputException("Invariant violation");
        return input;
    }
}
