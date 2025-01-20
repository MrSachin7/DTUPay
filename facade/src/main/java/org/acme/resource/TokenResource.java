package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.dto.GenerateTokenResponse;
import org.acme.service.TokenService;

@Path("/customers")
public class TokenResource {

    private final TokenService tokenService;

    public TokenResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GET
    @Produces("application/json")
    @Path("/{customerId}/tokens")
    public Response generateToken(@PathParam("customerId") String customerId, @QueryParam("amount") int amount) {
        try {
            GenerateTokenResponse token = tokenService.generateToken(customerId, amount);
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}