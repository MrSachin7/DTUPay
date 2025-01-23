package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.dto.GenerateReportsResponse;
import org.acme.dto.StartPaymentRequest;
import org.acme.service.PaymentService;
import org.acme.service.ReportService;

@Path("/merchants")
public class PaymentResource {

    private final PaymentService paymentService;
    private final ReportService reportService;

    public PaymentResource(PaymentService paymentService, ReportService reportService) {
        this.paymentService = paymentService;
        this.reportService = reportService;
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

}
