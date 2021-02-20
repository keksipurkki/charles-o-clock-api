package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Schema(description = "Phone number")
public class PhoneNumber {
    @NotBlank
    String phoneNumber;
}
