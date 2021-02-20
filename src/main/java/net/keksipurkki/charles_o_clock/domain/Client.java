package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.security.KeyPair;
import java.util.Date;

@Value
@Schema(description = "Charles O'Clock client")
public class Client {
    String id;
    KeyPair keypair;
    Date registered;
    Date confirmed;
}
