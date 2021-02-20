package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.security.PrivateKey;

@Value
@Schema(description = "Client credentials")
public class ClientCredentials {
    String id;
    PrivateKey secret;
}
