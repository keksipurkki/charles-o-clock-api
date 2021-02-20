package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Schema
@Value
public class Tag {
    UUID id;
    Client client;
}
