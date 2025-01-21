package steps;

import core.domain.account.*;
import core.domainService.AccountService;
import eventConsumer.RegisterCustomerRequestProcessor;
import eventConsumer.ValidateTokenCompletedProcessor;
import events.AccountValidationCompleted;
import events.RegisterCustomerCompleted;
import events.RegisterCustomerRequested;
import events.ValidateTokenCompleted;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.mockito.Mockito;
import persistence.AccountRepositoryImpl;

import java.util.UUID;
import java.util.concurrent.CompletionStage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenServiceSteps {

    private final AccountRepository accountRepository;
    private final ValidateTokenCompletedProcessor validateTokenCompletedProcessor;
    private final Emitter<AccountValidationCompleted> accountValidationCompletedEmitter;

    private RegisterCustomerRequested registerCustomerRequested;
    private ValidateTokenCompleted validateTokenCompleted;
    private AccountValidationCompleted accountValidationCompleted;
    private RegisterCustomerCompleted registerCustomerCompleted;
    private final RegisterCustomerRequestProcessor registerCustomerRequestProcessor;

    public TokenServiceSteps() {
        this.accountRepository = new AccountRepositoryImpl();
        AccountService accountService = new AccountService(accountRepository);
        this.registerCustomerRequestProcessor = new RegisterCustomerRequestProcessor(accountService);

        this.accountValidationCompletedEmitter = mock(Emitter.class);

        this.validateTokenCompletedProcessor = new ValidateTokenCompletedProcessor(accountService, accountValidationCompletedEmitter);
    }

    @Given("a customer in the system")
    public void aCustomerInTheSystem() {
        registerCustomerRequested = new RegisterCustomerRequested(
                UUID.randomUUID().toString(), "Satish", "Grg", "1234567891", "353535"
        );
        JsonObject request = JsonObject.mapFrom(registerCustomerRequested);
        registerCustomerCompleted = registerCustomerRequestProcessor.process(request);
    }

    @And("the customer has a valid token")
    public void theCustomerHasAValidToken() {
        String customerId = registerCustomerCompleted.getCustomerId();
        validateTokenCompleted = new ValidateTokenCompleted("123", customerId, null);
    }

    @When("the validate token is requested with the existing customer")
    public void theValidateTokenIsRequestedWithTheExistingCustomer() {
        JsonObject request = JsonObject.mapFrom(validateTokenCompleted);
        validateTokenCompletedProcessor.process(request);
    }

    @Then("the token should be successfully validated")
    public void theTokenShouldBeSuccessfullyValidated() {
        assertNotNull(accountValidationCompleted);
        assertNotNull(accountValidationCompleted.getBankAccountNumber(), "The bank account number should be valid.");
        assertEquals(validateTokenCompleted.getCustomerId(), accountValidationCompleted.getUserId(), "The customer ID should be correct.");
    }

    @And("the event should be fired with same correlation id for validate token")
    public void theEventShouldBeFiredWithSameCorrelationId() {
        // Verify that the event was sent with the correct correlation ID
        verify(accountValidationCompletedEmitter, times(1)).send(any(AccountValidationCompleted.class));
    }

    @And("the event should consist of the correct customer id")
    public void theEventShouldConsistOfTheCorrectCustomerId() {
      accountValidationCompleted.getUserId(),validateTokenCompleted.getCustomerId()
        ));
    }
}
