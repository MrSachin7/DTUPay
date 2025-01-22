package org.acme.service;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.events.UnregisterCustomerCompleted;
import org.acme.events.UnregisterCustomerRequested;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class UnregisterCustomerService {

    private final Emitter<UnregisterCustomerRequested> customerRequestEmitter;

    private final ConcurrentHashMap<String, CompletableFuture<Void>> coRelations = new ConcurrentHashMap<>();

    public UnregisterCustomerService(@Channel("UnregisterCustomerRequested") Emitter<UnregisterCustomerRequested> customerRequestEmitter) {
        this.customerRequestEmitter = customerRequestEmitter;
    }

    public void unregisterCustomer(String customerId) {
        // Create and store the future BEFORE sending the request
        UnregisterCustomerRequested event = new UnregisterCustomerRequested(UUID.randomUUID().toString(), customerId);


        CompletableFuture<Void> responseFuture = new CompletableFuture<>();
        coRelations.put(event.getCoRelationId(), responseFuture);

        try {
            // Send the request
            System.out.println("Sending request to unregister customer");
            customerRequestEmitter.send(event);

            // Wait for the response with a timeout
            // Wait for 30 secs at max
            responseFuture.get(30, TimeUnit.SECONDS);

        } catch (Exception e) {
            // Clean up the map entry in case of failure
            coRelations.remove(event.getCoRelationId());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Incoming("UnregisterCustomerCompleted")
    public void process(JsonObject response) {
        UnregisterCustomerCompleted event = response.mapTo(UnregisterCustomerCompleted.class);

        CompletableFuture<Void> future = coRelations.remove(event.getCoRelationId());  // Remove while getting

        if (future == null) {
            System.out.println("No future found for correlation id: " + event.getCoRelationId());
            return;
        }

        if (!event.wasSuccessful()) {
            System.out.println("Failed to register customer: " + event.getError());
            future.completeExceptionally(new RuntimeException(event.getError()));
        }
        future.complete(null);
    }

}
