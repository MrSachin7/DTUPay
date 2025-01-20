package org.acme.service;

import org.acme.events.PaymentRequested;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class PaymentService {
    private final Emitter<PaymentRequested> paymentRequestEmitter;

    public PaymentService(@Channel("payment-requests") Emitter<PaymentRequested> paymentRequestEmitter) {
        this.paymentRequestEmitter = paymentRequestEmitter;
    }

    public void sendPaymentRequest() {
        // Hardcoded values to test
        String correlationId = UUID.randomUUID().toString();
        String token = "hardcoded-token";
        String merchantId = "hardcoded-merchant";
        double amount = 100.0;

        PaymentRequested event = new PaymentRequested(correlationId, token, merchantId, amount);

        System.out.println("Sending PaymentRequested event: " + event);
        paymentRequestEmitter.send(event);
    }
}
