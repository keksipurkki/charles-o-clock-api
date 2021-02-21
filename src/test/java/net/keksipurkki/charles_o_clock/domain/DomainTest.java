package net.keksipurkki.charles_o_clock.domain;

import net.keksipurkki.charles_o_clock.exception.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DomainTest {

    @Test
    public void phonenumber_given_falsey_inputs_then_invalid_input_exception() {
        assertThrows(InvalidInputException.class, () -> new PhoneNumber(null));
        assertThrows(InvalidInputException.class, () -> new PhoneNumber(" "));
        assertThrows(InvalidInputException.class, () -> new PhoneNumber(""));
    }

}