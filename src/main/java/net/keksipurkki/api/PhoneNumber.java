package net.keksipurkki.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import static java.util.Objects.isNull;

@Value
@Schema(description = "Phone number")
public class PhoneNumber {
  String number;
  public PhoneNumber(String number) {
    this.number = number;
    if (isNull(this.number) || this.number.isBlank())
      throw new IllegalArgumentException("Invalid phone number");
  }
}
