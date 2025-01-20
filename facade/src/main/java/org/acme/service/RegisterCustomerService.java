package org.acme.service;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;
import org.acme.events.RegisterCustomerCompleted;
import org.acme.events.RegisterCustomerRequested;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class RegisterCustomerService {

    private final Emitter<RegisterCustomerRequested> customerRequestEmitter;

    private final ConcurrentHashMap<String, CompletableFuture<String>> coRelations = new ConcurrentHashMap<>();

    public RegisterCustomerService(@Channel("RegisterCustomerRequested") Emitter<RegisterCustomerRequested> customerRequestEmitter) {
        this.customerRequestEmitter = customerRequestEmitter;
    }

    public RegisterCustomerResponse registerCustomer(RegisterCustomerRequest registerCustomerDto) {
        // Create and store the future BEFORE sending the request
        RegisterCustomerRequested event = new RegisterCustomerRequested(UUID.randomUUID().toString(),
                registerCustomerDto.firstname(), registerCustomerDto.lastname(),
                registerCustomerDto.cprNumber(),
                registerCustomerDto.accountNumber());


        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        coRelations.put(event.getCoRelationId(), responseFuture);

        try {
            // Send the request
            System.out.println("Sending request to register customer");
            customerRequestEmitter.send(event);

            // Wait for the response with a timeout
            // Wait for 30 secs at max
            String customerId = responseFuture.get(30, TimeUnit.SECONDS);
            return new RegisterCustomerResponse(customerId);

        } catch (Exception e) {
            // Clean up the map entry in case of failure
            coRelations.remove(event.getCoRelationId());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Incoming("RegisterCustomerCompleted")
    public void process(JsonObject response) {
        RegisterCustomerCompleted event = response.mapTo(RegisterCustomerCompleted.class);

        System.out.println("Received event for customer registration" + event.getCustomerId());
        CompletableFuture<String> future = coRelations.remove(event.getCoRelationId());  // Remove while getting

        if (future == null) {
            System.out.println("No future found for correlation id: " + event.getCoRelationId());
            return;
        }

        if (!event.wasSuccessful()) {
            System.out.println("Failed to register customer: " + event.getError());
            future.completeExceptionally(new RuntimeException(event.getError()));
        }
        future.complete(event.getCustomerId());
    }

}