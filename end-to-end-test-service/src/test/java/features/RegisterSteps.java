package features;

import dto.RegisterCustomerRequest;
import dto.RegisterCustomerResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import services.CustomerService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RegisterSteps {


    private final CustomerService customerService;

    private RegisterCustomerRequest request;
    private RegisterCustomerResponse registerCustomerResponse;

    private Exception registerException;

    public RegisterSteps(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Given("a customer with name {string}, last name {string}, and CPR {string} and bankAccountNumber {string}")
    public void aCustomerWithNameLastNameAndCPRAndBankAccountNumber(String firstname, String lastname, String cpr, String bankAccount) {
        request = new RegisterCustomerRequest(firstname, lastname, cpr, bankAccount);

    }

    @When("the customer registers with Simple DTU Pay using their bank account")
    public void theCustomerRegistersWithSimpleDTUPayUsingTheirBankAccount() {
        try {
            registerCustomerResponse = customerService.registerCustomer(request);
            assertNotNull(registerCustomerResponse);

        } catch (Exception e) {
            registerException = e;
        }
    }

    @And("the customer should be assigned a random identifier")
    public void theCustomerShouldBeAssignedARandomIdentifier() {
        assertNotNull(registerCustomerResponse.id());
    }

    @Then("the registration should be successful")
    public void the_registration_should_be_successful() {
        assertNotNull(registerCustomerResponse);

        assertNull(registerException);
    }
    @Then("the registration should not be successful")
    public void theRegistrationShouldNotBeSuccessful() {
        assertNull(registerCustomerResponse);
        assertNotNull(registerException);
    }
}
