package net.keksipurkki.charles_o_clock.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.security.PrivateKey;

@Schema(description = "Client credentials")
public record ClientCredentials(String id, @JsonIgnore PrivateKey secret) {}
