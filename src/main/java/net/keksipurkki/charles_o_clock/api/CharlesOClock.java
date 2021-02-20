package net.keksipurkki.charles_o_clock.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.vertx.core.Future;
import net.keksipurkki.charles_o_clock.domain.*;

import javax.ws.rs.*;
import java.util.UUID;

@OpenAPIDefinition(info = @Info(
    title = "Charles O'Clock REST API",
    version = "1.0.0",
    description = "Clock-in and clock-out"
))
@Produces("application/json")
@Consumes("application/json")
public interface CharlesOClock {

    @Operation(operationId = "CREATE_TAG", summary = "Provision new tags", description = "")
    @POST @Path("/tags")
    Future<Tag> createTag();

    @Operation(operationId = "REGISTER_CLIENT", summary = "Register a client", description = "register")
    @POST @Path("/clients")
    Future<ClientCredentials> register(PhoneNumber phoneNumber);

    @Operation(operationId = "GET_CLIENT", summary = "Get client details", description = "client")
    @GET @Path("/clients/{uuid}")
    Future<Client> getById(@PathParam("uuid") UUID id);

    @Operation(operationId = "CLAIM_TAG", summary = "Claim tags for a client", description = "claim")
    @POST @Path("/clients/{id}/tags")
    Future<Tag> claim(Client client, Tag tag);

    @Operation(operationId = "DISCLAIM_TAG", summary = "Disclaim tags for a client", description = "disclaim")
    @DELETE @Path("/clients/{id}/tags")
    Future<Tag> disclaim(Client client, Tag tag);

    @Operation(operationId = "CLOCK", summary = "Clock in or clock out", description = "in")
    @PUT @Path("/tags/{id}")
    Future<ClockStatus> clock(ClockStatus status);


}