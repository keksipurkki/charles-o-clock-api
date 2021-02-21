package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.security.KeyPair;
import java.util.Date;

@Schema(description = "Charles O'Clock client")
public record Client(String id, KeyPair keyPair, Date registered, Date confirmed) {};
