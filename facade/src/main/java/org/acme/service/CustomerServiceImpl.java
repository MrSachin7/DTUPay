package org.acme.service;

import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;
import org.acme.events.RegisterCustomerRequested;
import org.acme.events.RegisterCustomerSucceeded;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class CustomerServiceImpl implements CustomerService {

    @Channel("RegisterCustomerRequested")
    Emitter<RegisterCustomerRequested> customerRequestEmitter;

    private final ConcurrentHashMap<String, CompletableFuture<String>> coRelations = new ConcurrentHashMap<>();

    @Override
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
            throw new RuntimeException("Failed to register customer", e);
        }
    }

    @Incoming("RegisterCustomerSucceeded")
    @Blocking
    public void process(JsonObject obj) {
        RegisterCustomerSucceeded response = obj.mapTo(RegisterCustomerSucceeded.class);
        System.out.println("Received response for customer registration"+ response.getCustomerId());
        CompletableFuture<String> future = coRelations.remove(response.getCoRelationId());  // Remove while getting
        if (future != null) {
            future.complete(response.getCustomerId());
        }
    }
}
