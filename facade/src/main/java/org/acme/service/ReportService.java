package org.acme.service;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.events.ReportsRequested;
import org.acme.events.ReportsRetrieved;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ReportService {

    private final Emitter<ReportsRequested> reportsRequestedEmitter;

    private final ConcurrentHashMap<String, CompletableFuture<String>>
            coRelations = new ConcurrentHashMap<>();

    public ReportService(@Channel("ReportsRequested") Emitter<ReportsRequested> reportsRequestedEmitter) {
        this.reportsRequestedEmitter = reportsRequestedEmitter;
    }

    public void getReportsForAllPayments() {
        ReportsRequested event  = new ReportsRequested(UUID.randomUUID().toString());
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        coRelations.put(event.getCorrelationId(), responseFuture);

        try {
            System.out.println("Sending request to retrieve report of all payments");
            reportsRequestedEmitter.send(event);
        } catch (Exception e) {
            coRelations.remove(event.getCorrelationId());
            throw new RuntimeException("Failed to retrieve report of all payments", e);
        }
    }

    @Incoming("ReportsRetrieved")
    public void process(JsonObject request) {
        ReportsRetrieved event = request.mapTo(ReportsRetrieved.class);
        CompletableFuture<List<Payment>> future = coRelations.remove(event.getCorrelationId());

        future.complete(event.getReport());
    }

}