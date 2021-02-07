package net.keksipurkki.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.Date;
import java.util.UUID;

@Value
@Schema(description = "Clock status")
public class ClockStatus {
  UUID id;
  Date timestamp;
  Action action;
  Client client;

  public enum Action {
    CLOCK_IN("CLOCK_IN"),
    CLOCK_OUT("CLOCK_OUT");
    public final String label;
    Action(String label) {
      this.label = label;
    }
  }

  public static ClockStatus clockIn(Client client) {
    return new ClockStatus(UUID.randomUUID(), new Date(), Action.CLOCK_IN, client);

  }

  public static ClockStatus clockOut(Client client) {
    return new ClockStatus(UUID.randomUUID(), new Date(), Action.CLOCK_OUT, client);
  }

}
