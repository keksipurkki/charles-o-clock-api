package net.keksipurkki.charles_o_clock.domain;

public record Violation(String property, String message, Object value) {}
