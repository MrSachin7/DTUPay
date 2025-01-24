package cucumbertests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import org.acme.dto.RegisterCustomerRequest;
import org.acme.dto.RegisterCustomerResponse;
import org.acme.events.RegisterCustomerCompleted;
import org.acme.events.RegisterCustomerRequested;
import org.acme.service.RegisterService;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author: Eduardo Filipe Fernandes Miranda (s223113)
 */
public class CustomerOrMerchantRegistrationSteps {
    private RegisterCustomerRequest customerRequest;
    private RegisterCustomerRequest customerRequest2;
    private RegisterCustomerRequested customerRequested;
    private RegisterCustomerRequested customerRequested2;
    private final RegisterService registerService;
    private final Emitter<RegisterCustomerRequested> emitter;

    private final CompletableFuture<RegisterCustomerResponse> registerCustomerResponse = new CompletableFuture<>();
    private final CompletableFuture<RegisterCustomerResponse> registerCustomerResponse2 = new CompletableFuture<>();

    public CustomerOrMerchantRegistrationSteps() {
        emitter = mock(Emitter.class);
        registerService = new RegisterService(emitter);

        synchronized (this) {
            when(emitter.send(any(RegisterCustomerRequested.class))).thenAnswer(invocation -> {
                RegisterCustomerRequested event = invocation.getArgument(0);
                RegisterCustomerCompleted completed = new RegisterCustomerCompleted(
                        event.getCoRelationId(),
                        generateCustomerId()
                );

                registerService.process(JsonObject.mapFrom(completed));

                return null;
            });
        }
    }

    private String generateCustomerId() {
        return UUID.randomUUID().toString();
    }

    @Given("a customer with firstname {string}, lastname {string}, CPR number {string}, and account number {string}")
    public void aCustomerWithFirstnameLastnameCPRNumberAndAccountNumber(String firstname, String lastname, String cprNumber, String accountNumber) {
        customerRequest = new RegisterCustomerRequest(firstname, lastname, cprNumber, accountNumber);
    }

    @When("the customer registers with DTUPay")
    public void theCustomerRegistersWithDTUPay() {
        customerRequested = new RegisterCustomerRequested(
                UUID.randomUUID().toString(),
                customerRequest.firstname(),
                customerRequest.lastname(),
                customerRequest.cprNumber(),
                customerRequest.accountNumber()
        );

        CompletableFuture.supplyAsync(() ->
                        registerService.register(customerRequest)
                ).thenAccept(registerCustomerResponse::complete)
                .exceptionally(ex -> {
                    registerCustomerResponse.completeExceptionally(ex);
                    return null;
                });
    }

    @Then("the RegisterCustomerRequested event is published")
    public void theRegisterCustomerRequestedEventIsPublished() {
        verify(emitter, times(1)).send(any(RegisterCustomerRequested.class));
    }

    @And("the event contains the correct data")
    public void theEventContainsTheCorrectData() {
        verify(emitter, times(1)).send((RegisterCustomerRequested) argThat(event -> {
            RegisterCustomerRequested firedEvent = (RegisterCustomerRequested) event;

            return firedEvent.getFirstname().equals(customerRequested.getFirstname()) &&
                    firedEvent.getLastname().equals(customerRequested.getLastname()) &&
                    firedEvent.getCprNumber().equals(customerRequested.getCprNumber()) &&
                    firedEvent.getAccountNumber().equals(customerRequested.getAccountNumber());
        }));
    }

    @When("the RegisterCustomerResponse is received with non-empty id")
    public void theRegisterCustomerResponseIsReceivedWithNonEmptyId() {
        RegisterCustomerResponse response = getResponseWithTimeout(registerCustomerResponse);
        assertNotNull(response.id());
    }

    @Then("the customer is registered")
    public void theCustomerIsRegistered() {
        RegisterCustomerResponse response = getResponseWithTimeout(registerCustomerResponse);
        assertNotNull(response.id());
    }

    @Given("another customer with firstname {string}, lastname {string}, CPR number {string}, and account number {string}")
    public void anotherCustomerWithFirstnameLastnameCPRNumberAndAccountNumber(String firstname, String lastname, String cprNumber, String accountNumber) {
        customerRequest2 = new RegisterCustomerRequest(firstname, lastname, cprNumber, accountNumber);
    }

    @When("the second customer registers with DTUPay")
    public void theSecondCustomerRegistersWithDTUPay() {
        customerRequested2 = new RegisterCustomerRequested(
                UUID.randomUUID().toString(),
                customerRequest2.firstname(),
                customerRequest2.lastname(),
                customerRequest2.cprNumber(),
                customerRequest2.accountNumber()
        );

        CompletableFuture.supplyAsync(() ->
                        registerService.register(customerRequest2)
                ).thenAccept(registerCustomerResponse2::complete)
                .exceptionally(ex -> {
                    registerCustomerResponse2.completeExceptionally(ex);
                    return null;
                });
    }

    @Then("the RegisterCustomerRequested event is published for the first customer")
    public void theRegisterCustomerRequestedEventIsPublishedForTheFirstCustomer() {
        verify(emitter, times(1)).send((RegisterCustomerRequested) argThat(event -> {
            RegisterCustomerRequested firedEvent = (RegisterCustomerRequested) event;

            return firedEvent.getFirstname().equals(customerRequested.getFirstname()) &&
                    firedEvent.getLastname().equals(customerRequested.getLastname()) &&
                    firedEvent.getCprNumber().equals(customerRequested.getCprNumber()) &&
                    firedEvent.getAccountNumber().equals(customerRequested.getAccountNumber());
        }));
    }

    @Then("the RegisterCustomerRequested event is published for the second customer")
    public void theRegisterCustomerRequestedEventIsPublishedForTheSecondCustomer() {
        verify(emitter, times(1)).send((RegisterCustomerRequested) argThat(event -> {
            RegisterCustomerRequested firedEvent = (RegisterCustomerRequested) event;

            return firedEvent.getFirstname().equals(customerRequested2.getFirstname()) &&
                    firedEvent.getLastname().equals(customerRequested2.getLastname()) &&
                    firedEvent.getCprNumber().equals(customerRequested2.getCprNumber()) &&
                    firedEvent.getAccountNumber().equals(customerRequested2.getAccountNumber());
        }));
    }

    @When("the RegisterCustomerResponse is received for the second customer with non-empty id")
    public void theRegisterCustomerResponseIsReceivedForTheSecondCustomerWithNonEmptyId() {
        RegisterCustomerResponse response = getResponseWithTimeout(registerCustomerResponse2);
        assertNotNull(response.id());
    }

    @When("the RegisterCustomerResponse is received for the first customer with non-empty id")
    public void theRegisterCustomerResponseIsReceivedForTheFirstCustomerWithNonEmptyId() {
        RegisterCustomerResponse response = getResponseWithTimeout(registerCustomerResponse);
        assertNotNull(response.id());
    }

    @Then("the second customer is registered")
    public void theSecondCustomerIsRegistered() {
        RegisterCustomerResponse response = getResponseWithTimeout(registerCustomerResponse2);
        assertNotNull(response.id());
    }

    @Then("the first customer is registered")
    public void theFirstCustomerIsRegistered() {
        RegisterCustomerResponse response = getResponseWithTimeout(registerCustomerResponse);
        assertNotNull(response.id());
    }

    // A helper method to handle response with timeout
    private RegisterCustomerResponse getResponseWithTimeout(CompletableFuture<RegisterCustomerResponse> future) {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            fail("Registration did not complete within expected time: " + e.getMessage());
            return null;
        }
    }
}