package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "User")
public record User(@NotNull PhoneNumber phoneNumber) { }