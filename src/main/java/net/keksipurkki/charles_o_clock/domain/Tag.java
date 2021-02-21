package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema
public record Tag(UUID id, Client client) {}
