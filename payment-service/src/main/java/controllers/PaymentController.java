package controllers;

import events.PaymentRequested;
import events.PaymentProcessed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/payments")
public class PaymentController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPayment(PaymentRequested request) {
        // Log the incoming request (for debugging)
        System.out.println("Received PaymentRequested: " + request);

        if (request.getTokenId() == null || request.getTokenId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Token ID is required").build();
        }
        if (request.getAmount() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Amount must be greater than zero").build();
        }

        // Simulate processing and create a PaymentProcessed object
        PaymentProcessed processed = new PaymentProcessed(
                request.getTokenId(),
                request.getMerchantId(),
                request.getAmount()
        );

        // Return the PaymentProcessed response
        return Response.ok(processed).build();
    }
}