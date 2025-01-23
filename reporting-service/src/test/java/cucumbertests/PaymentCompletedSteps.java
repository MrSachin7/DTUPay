package cucumbertests;

import core.domain.payment.Amount;
import core.domain.payment.Payment;
import core.domain.payment.PaymentRepository;
import core.domainService.PaymentService;
import eventConsumer.PaymentCompletedProcessor;
import events.PaymentCompleted;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

public class PaymentCompletedSteps {

    private final PaymentRepository repository;
    private final PaymentCompletedProcessor processor;
    private PaymentCompleted event;

    public PaymentCompletedSteps() {
        this.repository = mock(PaymentRepository.class);
        PaymentService paymentService = new PaymentService(repository);
        processor = new PaymentCompletedProcessor(paymentService);
    }

    @When("the system receives the payment completed event with successful payment")
    public void theSystemReceivesThePaymentCompletedEventWithSuccessfulPayment() {
        event = new PaymentCompleted(UUID.randomUUID().toString(),
                null,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                50,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        assertTrue(event.wasSuccessful());

        processor.process(JsonObject.mapFrom(event));
    }

    @Then("the system stores the payment in the repository")
    public void theSystemStoresThePaymentInTheRepository() {
        verify(repository, times(1)).add(argThat(payment -> {
            assertEquals(event.getPaymentId(), payment.getId().getValue());
            assertEquals(event.getToken(), payment.getToken().getValue());
            assertEquals(event.getMerchantId(), payment.getMerchantId().getValue());
            assertEquals(event.getCustomerId(), payment.getCustomerId().getValue());
            assertEquals(Amount.from(event.getAmount()), payment.getAmount());
            return true;
        }));
    }

    @When("the system receives the payment completed event with unsuccessful payment")
    public void theSystemReceivesThePaymentCompletedEventWithUnsuccessfulPayment() {
        event = new PaymentCompleted(UUID.randomUUID().toString(),
                "There was an error processing this payment",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                50,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        assertFalse(event.wasSuccessful());

        processor.process(JsonObject.mapFrom(event));
    }

    @Then("the system does not store the payment in the repository")
    public void theSystemDoesNotStoreThePaymentInTheRepository() {
        verify(repository, never()).add(any(Payment.class));
    }
}
