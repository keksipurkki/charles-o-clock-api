package net.keksipurkki.charles_o_clock.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.UUID;

@Schema(description = "Clock status")
public record ClockStatus(UUID id, Date timestamp, Action action, Client client) {

    public static ClockStatus clockIn(Client client) {
        return new ClockStatus(UUID
            .randomUUID(), new Date(), Action.CLOCK_IN, client);

    }

    public static ClockStatus clockOut(Client client) {
        return new ClockStatus(UUID
            .randomUUID(), new Date(), Action.CLOCK_OUT, client);
    }

}
