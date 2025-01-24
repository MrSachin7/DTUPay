package org.acme.service;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.GenerateReportsResponse;
import org.acme.dto.ReportData;
import org.acme.events.ReportType;
import org.acme.events.ReportsRequested;
import org.acme.events.ReportsRetrieved;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ReportService {

    private final Emitter<ReportsRequested> reportsRequestedEmitter;

    private final ConcurrentHashMap<String, CompletableFuture<List<ReportsRetrieved.PaymentData>>>
            coRelations = new ConcurrentHashMap<>();

    public ReportService(@Channel("ReportsRequested") Emitter<ReportsRequested> reportsRequestedEmitter) {
        this.reportsRequestedEmitter = reportsRequestedEmitter;
    }

    public GenerateReportsResponse getReportsForAllPayments() {
        ReportsRequested event = new ReportsRequested(UUID.randomUUID().toString());
        CompletableFuture<List<ReportsRetrieved.PaymentData>> responseFuture = new CompletableFuture<>();
        coRelations.put(event.getCorrelationId(), responseFuture);

        try {
            System.out.println("Sending request to retrieve report of all payments");
            reportsRequestedEmitter.send(event);

            // Wait for the response with a timeout
            // Wait for 30 secs at max
            List<ReportsRetrieved.PaymentData> paymentData = responseFuture.get(30, TimeUnit.SECONDS);
            return new GenerateReportsResponse(transformToReportData(paymentData));
        } catch (Exception e) {
            coRelations.remove(event.getCorrelationId());
            throw new RuntimeException("Failed to retrieve report of all payments", e);
        }
    }

    public GenerateReportsResponse getReportsForCustomer(String customerId) {
        ReportsRequested event = new ReportsRequested(UUID.randomUUID().toString(), ReportType.CUSTOMER_REPORTS,
                customerId);
        CompletableFuture<List<ReportsRetrieved.PaymentData>> responseFuture = new CompletableFuture<>();
        coRelations.put(event.getCorrelationId(), responseFuture);

        try {
            System.out.println("Sending request to retrieve report of customer payments");
            reportsRequestedEmitter.send(event);

            // Wait for the response with a timeout
            // Wait for 30 secs at max
            List<ReportsRetrieved.PaymentData> paymentData = responseFuture.get(30, TimeUnit.SECONDS);
            return new GenerateReportsResponse(transformToReportData(paymentData));
        } catch (Exception e) {
            coRelations.remove(event.getCorrelationId());
            throw new RuntimeException("Failed to retrieve report of all payments", e);
        }
    }

    public GenerateReportsResponse getReportsForMerchant(String merchantId) {
        ReportsRequested event = new ReportsRequested(UUID.randomUUID().toString(), ReportType.MERCHANT_REPORTS,
                merchantId);
        CompletableFuture<List<ReportsRetrieved.PaymentData>> responseFuture = new CompletableFuture<>();
        coRelations.put(event.getCorrelationId(), responseFuture);

        try {
            System.out.println("Sending request to retrieve report of merchant payments");
            reportsRequestedEmitter.send(event);

            // Wait for the response with a timeout
            // Wait for 30 secs at max
            List<ReportsRetrieved.PaymentData> paymentData = responseFuture.get(30, TimeUnit.SECONDS);
            return new GenerateReportsResponse(transformToReportData(paymentData));
        } catch (Exception e) {
            coRelations.remove(event.getCorrelationId());
            throw new RuntimeException("Failed to retrieve report of all payments", e);
        }
    }

    @Incoming("ReportsRetrieved")
    public void process(JsonObject response) {
        System.out.println("Received response for report request");
        ReportsRetrieved event = response.mapTo(ReportsRetrieved.class);
        CompletableFuture<List<ReportsRetrieved.PaymentData>> future = coRelations.remove(event.getCorrelationId());

        future.complete(event.getPaymentData());
    }

    private List<ReportData> transformToReportData(List<ReportsRetrieved.PaymentData> paymentData) {
        return paymentData.stream()
                .map(data -> new ReportData(data.getMerchantId(), data.getToken(), data.getAmount(), data.getPaymentId(), data.getCustomerId()))
                .toList();
    }
}