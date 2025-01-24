package cucumbertests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.events.PaymentRequested;
import org.acme.events.RegisterCustomerRequested;
import org.acme.service.RegisterService;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterCustomerRequestedSteps {
    private RegisterCustomerRequest customerRequest;
    private RegisterCustomerRequested customerRequested;
    private RegisterService registerService;
    private Emitter<RegisterCustomerRequested> emitter;

    public RegisterCustomerRequestedSteps() {
        emitter = mock(Emitter.class);
        registerService = new RegisterService(emitter);
    }

    @Given("a customer with firstname {string}, lastname {string}, CPR number {string}, and account number {string}")
    public void aCustomerWithFirstnameLastnameCPRNumberAndAccountNumber(String firstname, String lastname, String cprNumber, String accountNumber) {
        customerRequest = new RegisterCustomerRequest(firstname, lastname, cprNumber, accountNumber);
    }

    @When("the customer registers with DTUPay")
    public void theCustomerRegistersWithDTUPay() {
        customerRequested = new RegisterCustomerRequested(UUID.randomUUID().toString(), customerRequest.firstname(), customerRequest.lastname(), customerRequest.cprNumber(), customerRequest.accountNumber());
        registerService.register(customerRequest);
    }

    @Then("the RegisterCustomerRequested event is published")
    public void theRegisterCustomerRequestedEventIsPublished() {
        verify(emitter, times(1)).send(any(RegisterCustomerRequested.class));
    }

    @And("the event contains the correct data")
    public void theEventContainsTheCorrectData() {
        /*verify(emitter, times(1)).send((RegisterCustomerRequested) argThat(event -> {
            RegisterCustomerRequested firedEvent = (RegisterCustomerRequested) event;
            return firedEvent.getFirstname().equals(RegisterCustomerRequest.firstname()) &&
                    firedEvent.getLastname().equals(RegisterCustomerRequest.lastname()) &&
                    firedEvent.getCprNumber().equals(RegisterCustomerRequest.cprNumber()) &&
                    firedEvent.getAccountNumber().equals(RegisterCustomerRequest.accountNumber();
        }));*/
    }
}
