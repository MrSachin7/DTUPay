package cucumbertests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import org.acme.dto.GenerateReportsResponse;
import org.acme.events.ReportType;
import org.acme.events.ReportsRequested;
import org.acme.events.ReportsRetrieved;
import org.acme.service.ReportService;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author: Satish Gurung (s243872)
 */
public class RequestReportsSteps {
    private final ReportService reportService;
    private final Emitter<ReportsRequested> emitter;
    private final CompletableFuture<GenerateReportsResponse> reportsResponseFuture = new CompletableFuture<>();
    private String customerId;
    private String merchantId;

    public RequestReportsSteps() {
        emitter = mock(Emitter.class);
        reportService = new ReportService(emitter);

        synchronized (this) {
            when(emitter.send(any(ReportsRequested.class))).thenAnswer(invocation -> {
                ReportsRequested event = invocation.getArgument(0);
                ReportsRetrieved retrievedEvent = new ReportsRetrieved(
                        event.getCorrelationId(),
                        generateMockPaymentData(event.getReportType(), event.getId())
                );

                reportService.process(JsonObject.mapFrom(retrievedEvent));

                return null;
            });
        }
    }

    @When("a manager requests reports for all payments")
    public void aManagerRequestsReportsForAllPayments() {
        CompletableFuture.supplyAsync(reportService::getReportsForAllPayments
                ).thenAccept(reportsResponseFuture::complete)
                .exceptionally(ex -> {
                    reportsResponseFuture.completeExceptionally(ex);
                    return null;
                });
    }

    @Then("the ReportsRequested event is published")
    public void theReportsRequestedEventIsPublished() {
        verify(emitter, times(1)).send(any(ReportsRequested.class));
    }

    @And("the ReportsRequested event contains the correct data")
    public void theReportsRequestedEventContainsTheCorrectData() {
        verify(emitter, times(1)).send((ReportsRequested) argThat(event -> {
            ReportsRequested firedEvent = (ReportsRequested) event;

            return firedEvent.getReportType() == ReportType.ALL_REPORTS && firedEvent.getId() == null;
        }));
    }

    @When("the ReportsResponse is received")
    public void theReportsResponseIsReceived() {
        GenerateReportsResponse reportsResponse = getResponseWithTimeout(reportsResponseFuture);
        assertNotNull(reportsResponse);
        assertFalse(reportsResponse.payments().isEmpty());
    }

    @Then("the reports are returned")
    public void theReportsAreReturned() {
        GenerateReportsResponse reportsResponse = getResponseWithTimeout(reportsResponseFuture);
        assertTrue(reportsResponse.payments().size() > 0);
    }

    @Given("a customer is registered at DTUPay")
    public void aCustomerIsRegisteredAtDTUPay() {
        customerId = UUID.randomUUID().toString();
    }

    @When("the customer requests reports payments")
    public void theCustomerRequestsReportsPayments() {
        CompletableFuture.supplyAsync(() ->
                        reportService.getReportsForCustomer(customerId)
                ).thenAccept(reportsResponseFuture::complete)
                .exceptionally(ex -> {
                    reportsResponseFuture.completeExceptionally(ex);
                    return null;
                });
    }

    @And("the event contains the correct customer data")
    public void theEventContainsTheCorrectCustomerData() {
        verify(emitter, times(1)).send((ReportsRequested) argThat(event -> {
            ReportsRequested firedEvent = (ReportsRequested) event;

            return firedEvent.getReportType() == ReportType.CUSTOMER_REPORTS &&
                    firedEvent.getId().equals(customerId);
        }));
    }

    @Given("a merchant is registered at DTUPay")
    public void aMerchantIsRegisteredAtDTUPay() {
        merchantId = UUID.randomUUID().toString();
    }

    @When("the merchant requests reports payments")
    public void theMerchantRequestsReportsPayments() {
        CompletableFuture.supplyAsync(() ->
                        reportService.getReportsForMerchant(merchantId)
                ).thenAccept(reportsResponseFuture::complete)
                .exceptionally(ex -> {
                    reportsResponseFuture.completeExceptionally(ex);
                    return null;
                });
    }

    @And("the event contains the correct merchant data")
    public void theEventContainsTheCorrectMerchantData() {
        verify(emitter, times(1)).send((ReportsRequested) argThat(event -> {
            ReportsRequested firedEvent = (ReportsRequested) event;

            return firedEvent.getReportType() == ReportType.MERCHANT_REPORTS &&
                    firedEvent.getId().equals(merchantId);
        }));
    }

    private List<ReportsRetrieved.PaymentData> generateMockPaymentData(ReportType reportType, String entityId) {
        List<ReportsRetrieved.PaymentData> paymentData = new ArrayList<>();
        ReportsRetrieved.PaymentData data = new ReportsRetrieved.PaymentData();
        data.setPaymentId(UUID.randomUUID().toString());
        data.setCustomerId(reportType == ReportType.CUSTOMER_REPORTS ? entityId : UUID.randomUUID().toString());
        data.setMerchantId(reportType == ReportType.MERCHANT_REPORTS ? entityId : UUID.randomUUID().toString());
        data.setToken(UUID.randomUUID().toString());
        data.setAmount(100.0);
        data.setTimeStamp("24:00");
        paymentData.add(data);

        return paymentData;
    }

    // A helper method to handle response with timeout
    private GenerateReportsResponse getResponseWithTimeout(CompletableFuture<GenerateReportsResponse> future) {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            fail("Report retrieval did not complete within expected time: " + e.getMessage());
            return null;
        }
    }
}