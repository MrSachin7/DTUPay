package cucumbertests;

import core.domain.token.Customer;
import core.domain.token.CustomerId;
import core.domain.token.CustomerRepository;
import core.domain.token.TokenException;
import core.domainService.TokenService;
import eventConsumer.GenerateTokenRequestProcessor;
import events.GenerateTokenCompleted;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;


public class GenerateTokenSteps {

    private final CustomerRepository repository;

    private final Customer existingCustomer = Customer.from(CustomerId.newCustomerId());

    private final GenerateTokenRequestProcessor processor;

    private GenerateTokenCompleted event;

    private final String coRelationId = UUID.randomUUID().toString();


    public GenerateTokenSteps() {
        this.repository = mock(CustomerRepository.class);
        TokenService tokenService = new TokenService(repository);
        processor = new GenerateTokenRequestProcessor(tokenService);
    }

    @Given("a customer exits in the system")
    public void a_customer_exits_in_the_system() {
        // Mocking that the customer exists in the system
        when(repository.find(existingCustomer.getId())).thenReturn(existingCustomer);
    }

    @Given("a customer does not exits in the system")
    public void aCustomerDoesNotExitsInTheSystem() {
        // Mocking that the customer does not exist in the system
        when(repository.find(existingCustomer.getId())).thenReturn(null);
    }

    @And("the customer has never generated a token")
    public void theCustomerHasNeverGeneratedAToken() {
        // Nothing to do here
    }

    @And("the customer already has {int} token")
    public void theCustomerHasAlreadyHasToken(int tokenAmount) throws TokenException {
        existingCustomer.generateTokens(tokenAmount);

        // Assert that the customer has the correct number of tokens
        assertEquals(tokenAmount, existingCustomer.getTokens().size());
    }

    @When("the customer generates a tokens with amount {int}")
    public void theCustomerGeneratesATokensWithAmount(int amount) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("customerId", existingCustomer.getId().getValue());
        jsonObject.put("amount", amount);
        jsonObject.put("coRelationId", coRelationId);
        event = processor.process(jsonObject);
    }

    @Then("the tokens should be generated successfully")
    public void theTokensShouldBeGeneratedSuccessfully() {
        assertNotNull(event);
        assertTrue(event.wasSuccessful());
    }

    @Then("the tokens should not be generated")
    public void theTokensShouldNotBeGenerated() {
        assertNotNull(event);
        // Event was not successful
        assertFalse(event.wasSuccessful());

        // An error message exists
        assertNotNull(event.getError());

    }

    @And("the event is created with the same coRelation id")
    public void theEventIsCreatedWithTheSameCoRelationId() {
        assertEquals(coRelationId, event.getCoRelationId());
    }



}
