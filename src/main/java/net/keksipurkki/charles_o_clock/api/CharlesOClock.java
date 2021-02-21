package net.keksipurkki.charles_o_clock.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.vertx.core.Future;
import net.keksipurkki.charles_o_clock.domain.PhoneNumber;
import net.keksipurkki.charles_o_clock.domain.Tag;
import net.keksipurkki.charles_o_clock.domain.User;

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

    @Operation(operationId = "REGISTER")
    @POST @Path("/users")
    Future<User> register(PhoneNumber phoneNumber);

    @Operation(operationId = "GET_USER_BY_ID")
    @GET @Path("/users/{uuid}")
    Future<User> getById(@PathParam("uuid") UUID id);

    @Operation(operationId = "ADD_TAG")
    @POST @Path("/users/{uuid}/tags")
    Future<Tag[]> addTag(User user, Tag tag);

}