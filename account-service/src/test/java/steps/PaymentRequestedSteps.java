package steps;

import core.domain.account.*;
import core.domainService.AccountService;
import eventConsumer.PaymentRequestedProcessor;
import events.AccountValidationCompleted;
import events.PaymentRequested;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaymentRequestedSteps {

    private final AccountRepository accountRepository;

    private final Account account = Account.newAccount(CprNumber.from("1234567890"), Name.from("John", "Doe"), BankAccountNumber.from("1234567890"));

    PaymentRequested requestedEvent;
    AccountValidationCompleted completedEvent;

    private final PaymentRequestedProcessor processor;

    public PaymentRequestedSteps() {
        this.accountRepository = mock(AccountRepository.class);
        this.processor = new PaymentRequestedProcessor(new AccountService(accountRepository));
    }

    @Given("that an merchant exists in the system")
    public void thatAnMerchantExistsInTheSystem() {
        when(accountRepository.find(account.getId())).thenReturn(account);
    }

    @Given("that an merchant does not exists in the system")
    public void thatAnMerchantDoesNotExistsInTheSystem() {
        // Does not exist
        when(accountRepository.find(account.getId())).thenReturn(null);

    }

    @When("the payment request is initiated with the merchant id")
    public void thePaymentRequestIsInitiatedWithTheExistingMerchantId() {
        requestedEvent = new PaymentRequested(UUID.randomUUID().toString(), UUID.randomUUID().toString(), account.getId().getValue().toString(), 100);
        completedEvent = processor.process(JsonObject.mapFrom(requestedEvent));
    }

    @Then("the payment request should be successfully processed")
    public void thePaymentRequestShouldBeSuccessfullyProcessed() {
        assertTrue(completedEvent.wasSuccessful());
    }

    @And("the payment completed event should be emitted")
    public void thePaymentCompletedEventShouldBeEmitted() {
        assertNotNull(completedEvent);
    }

    @And("the event should have the same correlation id as the payment request")
    public void theEventShouldHaveTheSameCorrelationIdAsThePaymentRequest() {
        assertEquals(requestedEvent.getCorrelationId(), completedEvent.getCorrelationId());
    }

    @And("the bank account number should be the same as the merchant's bank account number")
    public void theBankAccountNumberShouldBeTheSameAsTheMerchantSBankAccountNumber() {
        assertEquals(account.getBankAccountNumber().getValue(), completedEvent.getBankAccountNumber());
    }

    @Then("the payment request should not be successfully processed")
    public void thePaymentRequestShouldNotBeSuccessfullyProcessed() {
        assertFalse(completedEvent.wasSuccessful());
    }

    @And("the bank account number should be null")
    public void theBankAccountNumberShouldBeNull() {
        assertNull(completedEvent.getBankAccountNumber());
    }
}
