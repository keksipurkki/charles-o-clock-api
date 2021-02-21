package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "Phone number")
public record PhoneNumber(@NotNull @NotBlank String phoneNumber) {
}
