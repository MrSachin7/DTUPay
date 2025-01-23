package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.GenerateReportsResponse;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;
import org.acme.service.RegisterCustomerService;
import org.acme.service.ReportService;
import org.acme.service.UnregisterCustomerService;

@Path("/customers")
public class CustomerResource {
    private final RegisterCustomerService registerCustomerService;
    private final UnregisterCustomerService unregisterCustomerService;
    private final ReportService reportService;

    public CustomerResource(RegisterCustomerService registerCustomerService,
            UnregisterCustomerService unregisterCustomerService, ReportService reportService) {
        this.registerCustomerService = registerCustomerService;
        this.unregisterCustomerService = unregisterCustomerService;
        this.reportService = reportService;
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
  
    @DELETE
    @Path("/{customerId}")
    public Response unregisterCustomer(@PathParam("customerId") String customerId) {
        try {
            unregisterCustomerService.unregisterCustomer(customerId);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
