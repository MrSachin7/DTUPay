package steps;

import core.domain.account.*;
import core.domainService.AccountService;
import eventConsumer.RegisterCustomerRequestProcessor;
import eventConsumer.UnRegisterCustomerRequestProcessor;
import events.RegisterCustomerCompleted;
import events.RegisterCustomerRequested;
import events.UnregisterCustomerCompleted;
import events.UnregisterCustomerRequested;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import persistence.AccountRepositoryImpl;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: Satish Gurung (s243872)
 */
public class AccountServiceSteps {

    private final AccountRepository accountRepository;

    private final RegisterCustomerRequestProcessor registerCustomerRequestProcessor;
    private RegisterCustomerRequested registerCustomerRequested;
    private RegisterCustomerCompleted registerCustomerCompleted;

    private final UnRegisterCustomerRequestProcessor unregisterCustomerRequestProcessor;
    private UnregisterCustomerRequested unregisterCustomerRequested;
    private UnregisterCustomerCompleted unregisterCustomerCompleted;

    public AccountServiceSteps(){
        this.accountRepository = new AccountRepositoryImpl();
        AccountService accountService = new AccountService(accountRepository);
        this.registerCustomerRequestProcessor = new RegisterCustomerRequestProcessor(accountService);
        this.unregisterCustomerRequestProcessor = new UnRegisterCustomerRequestProcessor(accountService);
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

    @When("the user unregisters with the DTUPay")
    public void theUserUnregistersWithTheDTUPay() {
        JsonObject request = JsonObject.mapFrom(registerCustomerCompleted);
        unregisterCustomerCompleted =  unregisterCustomerRequestProcessor.process(request);
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

    @Then("the customer should be successfully unregistered")
    public void theCustomerShouldBeSuccessfullyUnregistered() {
        assertNotNull(unregisterCustomerCompleted);
        assertTrue(unregisterCustomerCompleted.wasSuccessful());
        assertNotNull(unregisterCustomerCompleted.getCustomerId());
    }

    @Then("the customer should be NOT successfully unregistered")
    public void theCustomerShouldBeNOTSuccessfullyUnregistered() {
        assertNotNull(unregisterCustomerCompleted);
        assertFalse(unregisterCustomerCompleted.wasSuccessful());
        assertNotNull(unregisterCustomerCompleted.getError());
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

    @Given("is already registered")
    public void givenUserIsAlreadyRegistered() {

        assertNotNull(accountRepository.find(AccountId.from(registerCustomerCompleted.getCustomerId())));
    }

    @Given("is NOT already registered")
    public void givenUserIsNotAlreadyRegistered() {
        assertNull(accountRepository.find(AccountId.from(registerCustomerCompleted.getCustomerId())));
    }

}
