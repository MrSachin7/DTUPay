package cucumbertests;

import core.domainService.PaymentService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import eventConsumer.PaymentProcessor;
import events.PaymentCompleted;
import events.PaymentRequested;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentRequestedTest {

    private final PaymentService paymentService;
    private final PaymentProcessor paymentProcessor;
    private final Emitter<PaymentCompleted> emitter;

    private PaymentRequested paymentRequested;
    private PaymentCompleted paymentCompleted;

    private final String customerId = "Satish11";
    private final String merchantId = "momoHouse96";
    private final String correlationId = UUID.randomUUID().toString();
    private final double amount = 10.00;

    public PaymentRequestedTest() {
        this.paymentService = mock(PaymentService.class);
        this.emitter = mock(Emitter.class);
        this.paymentProcessor = new PaymentProcessor(emitter, paymentService);
    }

    @Given("that a customer and a merchant exists in the system")
    public void aCustomerAndAMerchantExistsInTheSystem() throws BankServiceException_Exception {
        when(paymentService.processPayment(customerId, merchantId, amount)).thenReturn("payment123");
    }

    @And("the customer has a valid payment method")
    public void theCustomerHasAValidPaymentMethod() throws BankServiceException_Exception {
        when(paymentService.processPayment(customerId, merchantId, amount)).thenReturn("payment123");
    }

    @When("the payment request is initiated with the customerId and merchantId")
    public void thePaymentRequestIsInitiatedWithTheCustomerAndMerchantId() throws BankServiceException_Exception {
        paymentRequested = new PaymentRequested(correlationId, "token123", merchantId, amount);
        paymentProcessor.processPaymentRequest(JsonObject.mapFrom(paymentRequested));
        System.out.println("Payment requested with correlationId: " + paymentRequested.getCorrelationId());

    }

    @Then("the payment request should be successfully processed")
    public void thePaymentRequestShouldBeSuccessful() throws BankServiceException_Exception {
        verify(emitter, times(1)).send(any(PaymentCompleted.class));
    }

    @And("the payment completed event should be emitted")
    public void thePaymentCompletedEventShouldBeEmitted() {
        assertNotNull(paymentCompleted, "The PaymentCompleted event should be emitted");
    }

    @And("the event should have the same correlation id as the payment request")
    public void theEventShouldHaveSameCorrelationIdAsThePaymentRequest() {
        assertEquals(paymentRequested.getCorrelationId(), paymentCompleted.getCorrelationId(),
                "The correlation ID in the PaymentCompleted event should be the same as in the PaymentRequested event");
    }

    @And("the payment should be a valid payment")
    public void andThePaymentShouldHaveAValidPayment() {
        assertNotNull(paymentCompleted.getPaymentId(), "The PaymentCompleted event should have a valid paymentId");
        assertFalse(paymentCompleted.getPaymentId().isEmpty(), "The paymentId should not be empty");
    }
}
