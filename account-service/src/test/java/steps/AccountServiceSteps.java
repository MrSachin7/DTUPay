package steps;

import core.domain.account.*;
import core.domainService.AccountService;
import eventConsumer.RegisterCustomerRequestProcessor;
import events.RegisterCustomerCompleted;
import events.RegisterCustomerRequested;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import persistence.AccountRepositoryImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceSteps {

    private final AccountRepository accountRepository;
    private final RegisterCustomerRequestProcessor registerCustomerRequestProcessor;
    private RegisterCustomerRequested registerCustomerRequested;

    private RegisterCustomerCompleted registerCustomerCompleted;

    public AccountServiceSteps(){
        this.accountRepository = new AccountRepositoryImpl();
        AccountService accountService = new AccountService(accountRepository);
        this.registerCustomerRequestProcessor = new RegisterCustomerRequestProcessor(accountService);
    }

    @Given("a user with firstname {string}, lastname {string}, CPR number {string} and account number {string}")
    public void aUserWithFirstnameLastnameCPRNumberAndAccountNumber(String firstname, String lastname, String cprNumber, String accountNumber) {
        registerCustomerRequested = new RegisterCustomerRequested(UUID.randomUUID().toString(), firstname, lastname, cprNumber, accountNumber);
    }

    @When("the user registers with the DTUPay")
    public void theUserRegistersAccount() {
        JsonObject request = JsonObject.mapFrom(registerCustomerRequested);
        registerCustomerCompleted =  registerCustomerRequestProcessor.process(request);
    }

    @Then("the user should be successfully registered")
    public void theUserShouldBeSuccessfullyRegistered() {
        assertNotNull(registerCustomerCompleted);
        assertTrue(registerCustomerCompleted.wasSuccessful());
        assertNotNull(registerCustomerCompleted.getCustomerId());

    }

    @Then("the user should not be successfully registered")
    public void theUserShouldNotBeSuccessfullyRegistered() {

        assertNotNull(registerCustomerCompleted);
        assertFalse(registerCustomerCompleted.wasSuccessful());

        assertNotNull(registerCustomerCompleted.getError());
    }

    @Then("the account should have the correct CPR number {string}")
    public void theAccountShouldHaveTheCorrectCPRNumber(String expectedCprNumber) {
        Account retrievedAccount = accountRepository.find(AccountId.from(registerCustomerCompleted.getCustomerId()));
        assertEquals(expectedCprNumber, retrievedAccount.getCprNumber().getValue(), "The CPR number should be correct");
    }


    @And("the event should be fired with same correlation id")
    public void theEventShouldBeFiredWithSameCorrelationId() {
        assertEquals(registerCustomerRequested.getCoRelationId(), registerCustomerCompleted.getCoRelationId(), "The correlation id should be the same");
    }


}
