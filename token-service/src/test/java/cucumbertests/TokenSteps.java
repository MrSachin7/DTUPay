package cucumbertests;

import core.domain.token.Customer;
import core.domain.token.CustomerId;
import core.domain.token.CustomerRepository;
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


public class TokenSteps {

    private final CustomerRepository repository;

    private final Customer existingCustomer = Customer.from(CustomerId.newCustomerId());

    private final TokenService tokenService;

    private final GenerateTokenRequestProcessor processor;

    private GenerateTokenCompleted event;

    private final String coRelationId = UUID.randomUUID().toString();


    public TokenSteps() {
        this.repository = mock(CustomerRepository.class);
        this.tokenService = new TokenService(repository);
        processor = new GenerateTokenRequestProcessor(tokenService);
    }

    @Given("a customer exits in the system")
    public void a_customer_exits_in_the_system() {

        // Mocking that the customer exists in the system
        when(repository.find(existingCustomer.getId())).thenReturn(existingCustomer);
    }

    @And("the customer has never generated a token")
    public void theCustomerHasNeverGeneratedAToken() {
        // Nothing to do here
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

    @And("the event is created with the same coRelation id")
    public void theEventIsCreatedWithTheSameCoRelationId() {
        assertEquals(coRelationId, event.getCoRelationId());
    }
}
