package cucumbertests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import org.acme.dto.StartPaymentRequest;
import org.acme.events.PaymentCompleted;
import org.acme.events.PaymentRequested;
import org.acme.service.PaymentService;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MerchantPaymentSteps {

    private String merchantId;
    private String customerToken;
    private double amount;
    private final PaymentService paymentService;
    private final Emitter<PaymentRequested> emitter;
    private final CompletableFuture<String> paymentResponse = new CompletableFuture<>();
    private String paymentId;
    private String errorMessage = "Some error";
    private boolean simulatePaymentError = false;

    public MerchantPaymentSteps() {
        emitter = mock(Emitter.class);
        paymentService = new PaymentService(emitter);

        when(emitter.send(any(PaymentRequested.class))).thenAnswer(invocation -> {
            PaymentRequested event = invocation.getArgument(0);
            PaymentCompleted completedEvent;

            if (simulatePaymentError) {
                completedEvent = new PaymentCompleted(event.getCorrelationId(), UUID.randomUUID().toString(), errorMessage);
            } else {
                completedEvent = new PaymentCompleted(event.getCorrelationId(), UUID.randomUUID().toString(), null);
            }

            paymentService.process(JsonObject.mapFrom(completedEvent));
            return null;

        });
    }

    @Given("the merchant is registered with DTUPay")
    public void theMerchantIsRegisteredWithDTUPay() {
        merchantId = "validMerchant";
    }

    @And("the merchant has a valid token for a customer")
    public void theMerchantHasAValidTokenForACustomer() {
        customerToken = UUID.randomUUID().toString();
        amount = 100.0;
    }

    @When("the merchant requests the payment")
    public void theMerchantRequestsThePayment() {
        StartPaymentRequest request = new StartPaymentRequest(customerToken, amount);

        CompletableFuture.supplyAsync(() ->
                        paymentService.startPayment(merchantId, request)
                ).thenAccept(paymentResponse::complete)
                .exceptionally(ex -> {
                    paymentResponse.completeExceptionally(ex);
                    return null;
                });
    }

    @When("the merchant requests the invalid payment")
    public void theMerchantRequestsTheInvalidPayment() {
        StartPaymentRequest request = new StartPaymentRequest(customerToken, amount);
        simulatePaymentError = true;

        CompletableFuture.supplyAsync(() ->
                        paymentService.startPayment(merchantId, request)
                ).thenAccept(paymentResponse::complete)
                .exceptionally(ex -> {
                    paymentResponse.completeExceptionally(ex);
                    return null;
                });
    }

    @Then("the PaymentRequested event is emitted")
    public void thePaymentRequestedEventIsEmitted() {
        verify(emitter, times(1)).send(any(PaymentRequested.class));
    }

    @And("the PaymentRequested event contains the correct information")
    public void thePaymentRequestedEventContainsCorrectInformation() {
        verify(emitter, times(1)).send((PaymentRequested) argThat(event -> {
            PaymentRequested firedEvent = (PaymentRequested) event;
            return firedEvent.getMerchantId().equals(merchantId) &&
                    firedEvent.getToken().equals(customerToken) &&
                    firedEvent.getAmount() == amount;
        }));
    }

    @When("the PaymentCompleted event is received")
    public void thePaymentCompletedEventIsReceived() {
        paymentId = getResponseWithTimeout(paymentResponse);
    }

    @Then("paymentId is returned")
    public void paymentIdIsReturned() {
        assertNotNull(paymentId);
    }

    @Then("event completes with exception")
    public void eventCompletesWithException() {
        assertThrows(RuntimeException.class, () -> getResponseWithTimeout(paymentResponse));
    }

    // A helper method to handle response with timeout
    private String getResponseWithTimeout(CompletableFuture<String> future) {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            if (e.getCause().getCause().getMessage().contains(errorMessage)) {
                throw (RuntimeException) e;
            }
            fail("Payment did not complete within expected time: " + e.getMessage());
            return null;
        }
    }
}