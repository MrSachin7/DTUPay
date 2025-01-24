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
public class RegisterService {

    private final Emitter<RegisterCustomerRequested> customerRequestEmitter;

    private final ConcurrentHashMap<String, CompletableFuture<String>> coRelations = new ConcurrentHashMap<>();

    public RegisterService(@Channel("RegisterCustomerRequested") Emitter<RegisterCustomerRequested> customerRequestEmitter) {
        this.customerRequestEmitter = customerRequestEmitter;
    }

    public RegisterCustomerResponse register(RegisterCustomerRequest registerCustomerDto) {
        // Create and store the future BEFORE sending the request
        RegisterCustomerRequested event = new RegisterCustomerRequested(UUID.randomUUID().toString(),
                registerCustomerDto.firstname(), registerCustomerDto.lastname(),
                registerCustomerDto.cprNumber(),
                registerCustomerDto.accountNumber());


        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        coRelations.put(event.getCoRelationId(), responseFuture);

        try {
            // Send the request
            System.out.println("RegisterCustomerRequested event fired " + event.getCoRelationId());
            customerRequestEmitter.send(event);

            // Wait for the response with a timeout
            // Wait for 30 secs at max
            String customerId = responseFuture.get(30, TimeUnit.SECONDS);
            System.out.println("Returning registered customer " + customerId);
            return new RegisterCustomerResponse(customerId);

        } catch (Exception e) {
            // Clean up the map entry in case of failure
            coRelations.remove(event.getCoRelationId());
            System.out.println("Failed to register customer: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Incoming("RegisterCustomerCompleted")
    public void process(JsonObject response) {
        RegisterCustomerCompleted event = response.mapTo(RegisterCustomerCompleted.class);

        System.out.println("RegisterCustomerRequested event received" + event.getCustomerId());
        CompletableFuture<String> future = coRelations.remove(event.getCoRelationId());  // Remove while getting

        if (future == null) {
            return;
        }

        if (!event.wasSuccessful()) {
            future.completeExceptionally(new RuntimeException(event.getError()));
        }
        future.complete(event.getCustomerId());
    }

}

