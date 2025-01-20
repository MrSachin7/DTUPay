package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;
import org.acme.service.RegisterCustomerService;

@Path("/customers")
public class CustomerResource {
    private final RegisterCustomerService registerCustomerService;

    public CustomerResource(RegisterCustomerService registerCustomerService) {
        this.registerCustomerService = registerCustomerService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerCustomer(RegisterCustomerRequest registerCustomerRequest) {
        try {
            RegisterCustomerResponse response = registerCustomerService.registerCustomer(registerCustomerRequest);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{customerId}")
    public Response unregisterCustomer(@PathParam("customerId") String customerId) {
        try {
            registerCustomerService.unregisterCustomer(customerId);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
