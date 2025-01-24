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

/**
 * @author: Ari Sigþór Eiríksson (s232409)
 */
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
            System.out.println("PaymentRequest event fired " + event.getCorrelationId());
            paymentRequestedEmitter.send(event);

            String paymentId = responseFuture.get();
            System.out.println("Returning paymentId " + paymentId);
            return paymentId;
        } catch (Exception e) {
            coRelations.remove(event.getCorrelationId());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Incoming("PaymentCompleted")
    public void process(JsonObject request){
        PaymentCompleted event = request.mapTo(PaymentCompleted.class);
        System.out.println("PaymentCompleted event received " + event.getCorrelationId());
        CompletableFuture<String> future = coRelations.remove(event.getCorrelationId());

        if (future == null) return;

        if (!event.wasSuccessful()){
            System.out.println(event.getError());
            future.completeExceptionally(new RuntimeException(event.getError()));
        }
        future.complete(event.getPaymentId());
    }

}
