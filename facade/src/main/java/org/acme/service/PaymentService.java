package org.acme.service;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.StartPaymentRequest;
import org.acme.events.PaymentCompleted;
import org.acme.events.PaymentRequested;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class PaymentService {

    private final Emitter<PaymentRequested> paymentRequestedEmitter;

    private final ConcurrentHashMap<String, CompletableFuture<String>>
            coRelations = new ConcurrentHashMap<>();

    public PaymentService(@Channel("PaymentRequested") Emitter<PaymentRequested> paymentRequestedEmitter) {
        this.paymentRequestedEmitter = paymentRequestedEmitter;
    }

    public String startPayment(String merchantId, StartPaymentRequest request){
        PaymentRequested event = new PaymentRequested(UUID.randomUUID().toString(),request.token(), merchantId, request.amount());
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        coRelations.put(event.getCorrelationId(), responseFuture);

        try {
            System.out.println("Sending request to start payment");
            paymentRequestedEmitter.send(event);

            String paymentId = responseFuture.get();
            System.out.println("Payment started with id: " + paymentId);
            return paymentId;
        } catch (Exception e) {
            coRelations.remove(event.getCorrelationId());
            throw new RuntimeException("Failed to start payment", e);
        }
    }

    @Incoming("PaymentCompleted")
    public void process(JsonObject request){
        PaymentCompleted event = request.mapTo(PaymentCompleted.class);
        CompletableFuture<String> future = coRelations.remove(event.getCorrelationId());

        if (!event.wasSuccessful()){
            throw new RuntimeException(event.getError());
        }
        if (future != null){
            future.complete(event.getPaymentId());
        }
    }

}
