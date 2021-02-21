package net.keksipurkki.charles_o_clock.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Action {
    CLOCK_IN("CLOCK_IN"),
    CLOCK_OUT("CLOCK_OUT");

    @JsonValue
    public final String label;

    Action(String label) {
        this.label = label;
    }
}
