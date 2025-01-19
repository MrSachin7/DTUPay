package cucumbertests;

import core.domain.token.Customer;
import core.domain.token.CustomerId;
import core.domain.token.CustomerRepository;
import core.domain.token.TokenException;
import core.domainService.TokenService;
import eventConsumer.ValidateTokenRequestProcessor;
import events.ValidateTokenCompleted;
import events.ValidateTokenRequested;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en_scouse.An;
import io.cucumber.junit.CucumberOptions;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

public class ValidateTokenSteps {

    private final ValidateTokenRequestProcessor processor;
    private final CustomerRepository repository;

    private final Customer existingCustomer = Customer.from(CustomerId.newCustomerId());

    private ValidateTokenRequested requestEvent;
    private ValidateTokenCompleted responseEvent;

    public ValidateTokenSteps() {
        this.repository = mock(CustomerRepository.class);
        this.processor = new ValidateTokenRequestProcessor(new TokenService(repository));
    }

    @Given("a customer exits in the system for token validation")
    public void aCustomerExistsInTheSystemForTokenValidation() {
        when(repository.find(existingCustomer.getId())).thenReturn(existingCustomer);
    }

    @Given("a customer does not exits in the system for token validation")
    public void aCustomerDoesNotExitsInTheSystemForTokenValidation() {
        when(repository.find(existingCustomer.getId())).thenReturn(null);
    }

    @And("the customer has generated a token")
    public void theCustomerHasGeneratedAToken() throws TokenException {
        existingCustomer.generateTokens(5);

        // Ensure that the customer has 5 tokens
        assertEquals(5, existingCustomer.getTokens().size());
    }

    @When("the customer validates the token with a valid token")
    public void theCustomerValidatesTheTokenWithAValidToken() {
        String validToken = existingCustomer.getTokens().getFirst();
        requestEvent = new ValidateTokenRequested(UUID.randomUUID().toString(), existingCustomer.getId().getValue(), validToken);
        JsonObject request = JsonObject.mapFrom(requestEvent);
        responseEvent = processor.process(request);
    }

    @When("the customer validates the token with an invalid token")
    public void theCustomerValidatesTheTokenWithAnInvalidToken() {
        String invalidToken = UUID.randomUUID().toString();

        requestEvent = new ValidateTokenRequested(UUID.randomUUID().toString(), existingCustomer.getId().getValue(), invalidToken);
        JsonObject request = JsonObject.mapFrom(requestEvent);
        responseEvent = processor.process(request);
    }


    @Then("the token should be validated successfully")
    public void theTokenShouldBeValidatedSuccessfully() {
        assertNotNull(responseEvent);
        assertTrue(responseEvent.wasSuccessful());
    }

    @Then("the token should not be validated")
    public void theTokenShouldNotBeValidated() {
        assertNotNull(responseEvent);
        assertFalse(responseEvent.wasSuccessful());
    }

    @And("the event is created with the same coRelation id for token validation")
    public void theEventIsCreatedWithTheSameCoRelationIdForTokenValidation() {
        assertEquals(requestEvent.getCoRelationId(), responseEvent.getCoRelationId());
    }

}
