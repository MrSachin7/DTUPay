package org.acme.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;
import org.acme.service.CustomerService;

@Path("/customers")
public class CustomerResource {
    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(RegisterCustomerRequest registerCustomerRequest) {
        try {
            RegisterCustomerResponse response = customerService.registerCustomer(registerCustomerRequest);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
