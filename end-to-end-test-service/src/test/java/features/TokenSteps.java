package features;

import dto.GenerateTokenResponse;
import dto.RegisterCustomerRequest;
import dto.RegisterCustomerResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import services.CustomerService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: Ari Sigþór Eiríksson (s232409)
 */
public class TokenSteps {

    private final CustomerService customerService;

    private final RegisterCustomerRequest request = new RegisterCustomerRequest(
            "Sachin",
            "Baral",
            "1234567890",
            "1234567890");

    private RegisterCustomerResponse registeredCustomer;
    private GenerateTokenResponse generateTokenResponse;

    private Exception generateTokenException;

    public TokenSteps(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Given("a customer has a valid account in the DTU Pay system")
    public void a_customer_has_a_valid_account_in_the_DTU_Pay_system() throws Exception {
        registeredCustomer = customerService.registerCustomer(request);
        assertNotNull(registeredCustomer);
        assertNotNull(registeredCustomer.id());
        Users.dtuPayAccounts.add(registeredCustomer.id());
    }

    @Given("a customer does not have a valid account in the DTU Pay system")
    public void aCustomerDoesNotHaveAValidAccountInTheDTUPaySystem() {
        // This customer doesn't exist in the system
        registeredCustomer = new RegisterCustomerResponse(UUID.randomUUID().toString());
    }

    @And("the customer has never generated the token before")
    public void theCustomerHasNeverGeneratedTheTokenBefore() {
        // No actions needed
    }

    @When("the customer generates {int} tokens")
    public void theCustomerGeneratesTokens(int amount) {
        try {
            generateTokenResponse = customerService.generateToken(registeredCustomer.id(), amount);
            assertNotNull(generateTokenResponse);
        } catch (Exception e) {
            // This might fail
            generateTokenException = e;
        }
    }

    @Then("the token should be generated successfully")
    public void theTokenShouldBeGeneratedSuccessfully() {
        assertNotNull(generateTokenResponse);
        // no exception was thrown
        assertNull(generateTokenException);
    }


    @Then("the token should not be generated")
    public void theTokenShouldNotBeGenerated() {
        assertNull(generateTokenResponse);

        // an exception was thrown
        assertNotNull(generateTokenException);
    }

    @And("the customer already has more than one remaining token in the system")
    public void theCustomerAlreadyHasMoreThanOneRemainingTokenInTheSystem() throws Exception {
        GenerateTokenResponse tokenResponse = customerService.generateToken(registeredCustomer.id(), 2);
        assertNotNull(tokenResponse);
        assertEquals(2, tokenResponse.tokens().size());

    }


    @And("the customer has only one token left")
    public void theCustomerHasOnlyOneTokenLeft() throws Exception {
        GenerateTokenResponse tokenResponse = customerService.generateToken(registeredCustomer.id(), 1);
        assertNotNull(tokenResponse);
        assertEquals(1, tokenResponse.tokens().size());
    }

    @And("the customer should receive {int} tokens")
    public void theCustomerShouldReceiveTokens(int amount) {
        assertNotNull(generateTokenResponse);
        int actual = generateTokenResponse.tokens().size();
        System.out.println(actual);
        assertEquals(amount, actual);
    }

    @And("the customer should not receive any tokens")
    public void theCustomerShouldNotReceiveAnyTokens() {
        assertNull(generateTokenResponse);
    }


}
