package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import static net.keksipurkki.charles_o_clock.domain.Invariants.*;

@Schema(description = "Phone number")
public record PhoneNumber(String phoneNumber) {
    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = invariant(isNotBlank, phoneNumber);
    }
}
