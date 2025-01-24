package steps;

import core.domainService.PaymentService;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import eventConsumer.PaymentProcessor;
import events.AccountValidationCompleted;
import events.PaymentCompleted;
import events.PaymentRequested;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * @author: Janusz Jakub Wilczek (s243891)
 */
public class PaymentRequestedSteps {

    private final PaymentProcessor paymentProcessor;
    private final Emitter<PaymentCompleted> emitter;
    private PaymentRequested paymentRequested;
    private AccountValidationCompleted validatedCustomer;
    private BankService bankService;

    private AccountValidationCompleted validatedMerchant;

    public PaymentRequestedSteps() {
        emitter = mock(Emitter.class);
        bankService = mock(BankService.class);
        paymentProcessor = new PaymentProcessor(emitter, new PaymentService(bankService));
    }

    @When("the paymentRequested event is received")
    public void thePaymentRequestedEventIsReceived() {
        paymentRequested= new PaymentRequested(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), 10.00);
        JsonObject request = JsonObject.mapFrom(paymentRequested);
        paymentProcessor.processPaymentRequest(request);
    }

    @And("the ValidateCustomer event is received")
    public void theValidateCustomerEventIsReceived() {
        validatedCustomer = new AccountValidationCompleted(paymentRequested.getCorrelationId(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);
        JsonObject request = JsonObject.mapFrom(validatedCustomer);
        paymentProcessor.processCustomerValidation(request);

    }

    @And("the ValidateMerchant is received")
    public void theValidateMerchantIsReceived() {
        validatedMerchant = new AccountValidationCompleted(paymentRequested.getCorrelationId(), paymentRequested.getMerchantId(), UUID.randomUUID().toString(), null);
        JsonObject request = JsonObject.mapFrom(validatedMerchant);
        paymentProcessor.processMerchantValidation(request);
    }

    @Then("the payment request should be successfully processed")
    public void thePaymentRequestShouldBeSuccessfullyProcessed() throws BankServiceException_Exception {
        verify(bankService, times(1)).transferMoneyFromTo(
                eq(validatedCustomer.getBankAccountNumber()),
                eq(validatedMerchant.getBankAccountNumber()),
                eq(BigDecimal.valueOf(paymentRequested.getAmount())),
                anyString()
        );
    }

    @And("the payment completed event should be emitted")
    public void thePaymentCompletedEventShouldBeEmitted() {
        verify(emitter, times(1)).send(any(PaymentCompleted.class));
    }

    @And("the event should have the same correlation id as the payment request")
    public void theEventShouldHaveTheSameCorrelationIdAsThePaymentRequest() {
        verify(emitter, times(1)).send((PaymentCompleted) argThat(event -> {
            PaymentCompleted firedEvent = (PaymentCompleted) event;
            return firedEvent.getCorrelationId().equals(paymentRequested.getCorrelationId());
        }));
    }

    @And("the ValidateCustomer event is received with a failed event")
    public void theValidateCustomerEventIsReceivedWithAFailedEvent() {
        validatedCustomer = new AccountValidationCompleted(paymentRequested.getCorrelationId(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Failed");
        JsonObject request = JsonObject.mapFrom(validatedCustomer);
        paymentProcessor.processCustomerValidation(request);

    }

    @Then("the payment request should not be successfully processed")
    public void thePaymentRequestShouldNotBeSuccessfullyProcessed() throws BankServiceException_Exception {
        verify(bankService, never()).transferMoneyFromTo(
                anyString(),
                anyString(),
                any(BigDecimal.class),
                anyString()
        );
    }

    @And("the ValidateMerchant is received with a failed event")
    public void theValidateMerchantIsReceivedWithAFailedEvent() {
        validatedMerchant = new AccountValidationCompleted(paymentRequested.getCorrelationId(), paymentRequested.getMerchantId(), UUID.randomUUID().toString(), "Failed");
        JsonObject request = JsonObject.mapFrom(validatedMerchant);
        paymentProcessor.processMerchantValidation(request);
    }
}
