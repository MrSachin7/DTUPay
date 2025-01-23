package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.GenerateReportsResponse;
import org.acme.dto.GenerateTokenResponse;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;
import org.acme.service.RegisterService;
import org.acme.service.ReportService;
import org.acme.service.TokenService;
import org.acme.service.UnregisterService;

@Path("/customers")
public class CustomerResource {
    private final RegisterService registerService;
    private final UnregisterService unregisterService;
    private final ReportService reportService;
    private final TokenService tokenService;

    public CustomerResource(RegisterService registerService,
                            UnregisterService unregisterService, ReportService reportService, TokenService tokenService) {
        this.registerService = registerService;
        this.unregisterService = unregisterService;
        this.reportService = reportService;
        this.tokenService = tokenService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerCustomer(RegisterCustomerRequest registerCustomerRequest) {
        try {
            RegisterCustomerResponse response = registerService.register(registerCustomerRequest);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Consumes("application/json")
    @Path("/{customerId}/reports")
    public Response getReportsForCustomer(@PathParam("customerId") String customerId) {
        try {
            GenerateReportsResponse reportsForAllPayments = reportService.getReportsForCustomer(customerId);
            return Response.ok(reportsForAllPayments).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
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
  
    @DELETE
    @Path("/{customerId}")
    public Response unregisterCustomer(@PathParam("customerId") String customerId) {
        try {
            unregisterService.unregisterCustomer(customerId);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
