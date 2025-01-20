package org.acme.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.acme.dto.StartPaymentRequest;
import org.acme.events.PaymentRequested;
import org.acme.service.PaymentService;

@Path("/merchants")
public class PaymentResource {

    private final PaymentService paymentService;

    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    @Consumes("application/json")
    @Path("/{merchantId}/payments")
    public Response startPayment(@PathParam("merchantId") String merchantId , StartPaymentRequest paymentRequested) {
        try {
            String paymentId = paymentService.startPayment(merchantId, paymentRequested);
            return Response.ok(paymentId).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }
}
