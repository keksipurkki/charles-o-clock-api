package net.keksipurkki.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.ws.rs.*;
import java.util.Map;

@OpenAPIDefinition(info = @Info(
  title = "Charles O'Clock REST API",
  version = "1.0.0",
  description = "Clock-in and clock-out"
))
@Produces("application/json")
@Consumes("application/json")
public interface CharlesOClock {

  @Operation(summary = "Provision new tags", description = "")
  @POST @Path("/tags")
  Future<Tag> createTag();

  @POST @Path("/clients")
  @Operation(summary = "Register a client", description = "register")
  Future<ClientCredentials> register(PhoneNumber phoneNumber);

  @Operation(summary = "Claim tags for a client", description = "claim")
  @POST @Path("/clients/{id}/tags")
  Future<Tag> claim(Client client, Tag tag);

  @Operation(summary = "Disclaim tags for a client", description = "disclaim")
  @DELETE @Path("/clients/{id}/tags")
  Future<Tag> disclaim(Client client, Tag tag);

  @Operation(summary = "Clock in or clock out", description = "in")
  @PUT @Path("/tags/{id}")
  Future<ClockStatus> clock(ClockStatus status);

  default void handle(RoutingContext ctx) {

    final JsonObject operation = ctx.get("operation");
    final String operationId = operation.getString("operationId");

    System.out.println(ctx.vertx().eventBus());
    //ctx.vertx().eventBus().publish("TECH", Map.of("operationId", operationId));

    var action = switch (operationId) {
      case "createTag" -> createTag();
      case "register" -> register(null);
      case "claim" -> claim(null, null);
      case "disclaim" -> disclaim(null, null);
      case "clock" -> clock(null);
      default -> throw new UnsupportedOperationException("Unsupported API operation " + operationId);
    };

    action.onSuccess(ctx::json);

  }

  default void error(RoutingContext ctx) {
    ctx.vertx().eventBus().publish("YOLO", "XYZ");
    ctx.response().setStatusCode(500).end(ctx.failure().getClass().getName());
  }

}