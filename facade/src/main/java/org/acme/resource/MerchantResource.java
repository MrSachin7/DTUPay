package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.GenerateReportsResponse;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;
import org.acme.dto.StartPaymentRequest;
import org.acme.service.PaymentService;
import org.acme.service.RegisterService;
import org.acme.service.ReportService;
import org.acme.service.UnregisterService;

/**
 * @author: Sachin Baral (s243871)
 */
@Path("/merchants")
public class MerchantResource {

    private final PaymentService paymentService;
    private final ReportService reportService;

    private final RegisterService registerService;

    private final UnregisterService unregisterService;

    public MerchantResource(PaymentService paymentService, ReportService reportService, RegisterService registerService, UnregisterService unregisterService) {
        this.paymentService = paymentService;
        this.reportService = reportService;
        this.registerService = registerService;
        this.unregisterService = unregisterService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerMerchant(RegisterCustomerRequest registerCustomerRequest) {
        try {
            RegisterCustomerResponse response = registerService.register(registerCustomerRequest);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Consumes("application/json")
    @Path("/{merchantId}/payments")
    public Response pay(@PathParam("merchantId") String merchantId , StartPaymentRequest paymentRequested) {
        try {
            String paymentId = paymentService.startPayment(merchantId, paymentRequested);
            return Response.ok(paymentId).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Consumes("application/json")
    @Path("/{merchantId}/reports")
    public Response getReportsForMerchant(@PathParam("merchantId") String merchantId) {
        try {
            GenerateReportsResponse reportsForAllPayments = reportService.getReportsForMerchant(merchantId);
            return Response.ok(reportsForAllPayments).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{merchantId}")
    public Response unregisterMerchant(@PathParam("merchantId") String merchantId) {
        try {
            unregisterService.unregisterCustomer(merchantId);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
