package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.service.PaymentService;

@Path("/payments")
public class PaymentResource {
    private final PaymentService paymentService;

    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    @Produces("application/json")
    public Response triggerPayment() {
        paymentService.sendPaymentRequest();
        return Response.accepted("{\"status\": \"PaymentRequested event sent\"}").build();
    }
}
