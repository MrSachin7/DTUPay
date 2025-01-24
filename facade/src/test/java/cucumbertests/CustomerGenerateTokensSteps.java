package cucumbertests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import org.acme.dto.GenerateTokenResponse;
import org.acme.events.GenerateTokenCompleted;
import org.acme.events.GenerateTokenRequested;
import org.acme.service.TokenService;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerGenerateTokensSteps {

    private String customerId;
    private String customerId2;
    private int tokenAmount;
    private GenerateTokenRequested tokenRequestedEvent;
    private GenerateTokenRequested tokenRequestedEvent2;
    private final TokenService tokenService;
    private final Emitter<GenerateTokenRequested> emitter;
    private final CompletableFuture<GenerateTokenResponse> tokenResponse = new CompletableFuture<>();
    private final CompletableFuture<GenerateTokenResponse> tokenResponse2 = new CompletableFuture<>();

    public CustomerGenerateTokensSteps() {
        emitter = mock(Emitter.class);
        tokenService = new TokenService(emitter);

        when(emitter.send(any(GenerateTokenRequested.class))).thenAnswer(invocation -> {
            GenerateTokenRequested event = invocation.getArgument(0);
            GenerateTokenCompleted completedEvent = new GenerateTokenCompleted(
                    event.getCoRelationId(), null);
            completedEvent.setTokens(List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
            tokenService.process(JsonObject.mapFrom(completedEvent));

            return null;
        });
    }

    @Given("a customer has a valid account with DTUPay")
    public void aCustomerHasAValidAccountWithDTUPay() {
        customerId = "validCustomer";
        tokenAmount = 2;
    }

    @When("the customer requests tokens")
    public void theCustomerRequestsTokens() {
        tokenRequestedEvent = new GenerateTokenRequested(UUID.randomUUID().toString(), customerId, tokenAmount);

        CompletableFuture.supplyAsync(() ->
                        tokenService.generateToken(customerId, tokenAmount)
                ).thenAccept(tokenResponse::complete)
                .exceptionally(ex -> {
                    tokenResponse.completeExceptionally(ex);
                    return null;
                });
    }

    @Then("the GenerateTokenRequested event is emitted")
    public void theGenerateTokenRequestedEventIsEmitted() {
        verify(emitter, times(1)).send(any(GenerateTokenRequested.class));
    }

    @And("the GenerateTokenRequested event has the correct data")
    public void theGenerateTokenRequestedEventHasTheCorrectData() {
        verify(emitter, times(1)).send((GenerateTokenRequested) argThat(event -> {
            GenerateTokenRequested firedEvent = (GenerateTokenRequested) event;
            return firedEvent.getCustomerId().equals(tokenRequestedEvent.getCustomerId()) &&
                    firedEvent.getAmount() == tokenRequestedEvent.getAmount();
        }));
    }

    @And("the GenerateTokenCompleted is received")
    public void theGenerateTokenCompletedIsReceived() {
        GenerateTokenResponse response = getResponseWithTimeout(tokenResponse);
        assertNotNull(response.tokens());
        assertEquals(tokenAmount, response.tokens().size());
    }

    @Given("another customer with valid account")
    public void anotherCustomerWithValidAccount() {
        customerId2 = "anotherValidCustomer";
        tokenAmount = 2;
    }

    @When("the second customer requests tokens")
    public void theSecondCustomerRequestsTokens() {
        tokenRequestedEvent2 = new GenerateTokenRequested(UUID.randomUUID().toString(), customerId2, tokenAmount);

        CompletableFuture.supplyAsync(() ->
                        tokenService.generateToken(customerId2, tokenAmount)
                ).thenAccept(tokenResponse2::complete)
                .exceptionally(ex -> {
                    tokenResponse2.completeExceptionally(ex);
                    return null;
                });
    }

    @Then("the GenerateTokenRequested event is emitted for the first customer")
    public void theGenerateTokenRequestedEventIsEmittedForFirstCustomer() {
        verify(emitter, times(1)).send((GenerateTokenRequested) argThat(event -> {
            GenerateTokenRequested firedEvent = (GenerateTokenRequested) event;
            return firedEvent.getCustomerId().equals(tokenRequestedEvent.getCustomerId()) &&
                    firedEvent.getAmount() == tokenRequestedEvent.getAmount();
        }));
    }

    @Then("the GenerateTokenRequested event is emitted for the second customer")
    public void theGenerateTokenRequestedEventIsEmittedForSecondCustomer() {
        verify(emitter, times(1)).send((GenerateTokenRequested) argThat(event -> {
            GenerateTokenRequested firedEvent = (GenerateTokenRequested) event;
            return firedEvent.getCustomerId().equals(tokenRequestedEvent2.getCustomerId()) &&
                    firedEvent.getAmount() == tokenRequestedEvent2.getAmount();
        }));
    }

    @And("the GenerateTokenCompleted is received for the first customer")
    public void theGenerateTokenCompletedIsReceivedForFirstCustomer() {
        GenerateTokenResponse response = getResponseWithTimeout(tokenResponse);
        assertNotNull(response.tokens());
        assertEquals(tokenAmount, response.tokens().size());
    }

    @And("the GenerateTokenCompleted is received for the second customer")
    public void theGenerateTokenCompletedIsReceivedForSecondCustomer() {
        GenerateTokenResponse response = getResponseWithTimeout(tokenResponse2);
        assertNotNull(response.tokens());
        assertEquals(tokenAmount, response.tokens().size());
    }

    @And("the GenerateTokenRequested event for first customer has the correct data")
    public void theGenerateTokenRequestedEventForFirstCustomerHasTheCorrectData() {
        verify(emitter, times(1)).send((GenerateTokenRequested) argThat(event -> {
            GenerateTokenRequested firedEvent = (GenerateTokenRequested) event;
            return firedEvent.getCustomerId().equals(customerId) &&
                    firedEvent.getAmount() == tokenAmount;
        }));
    }

    @And("the GenerateTokenRequested event for second customer has the correct data")
    public void theGenerateTokenRequestedEventForSecondCustomerHasTheCorrectData() {
        verify(emitter, times(1)).send((GenerateTokenRequested) argThat(event -> {
            GenerateTokenRequested firedEvent = (GenerateTokenRequested) event;
            return firedEvent.getCustomerId().equals(customerId2) &&
                    firedEvent.getAmount() == tokenAmount;
        }));
    }

    // A helper method to handle response with timeout
    private GenerateTokenResponse getResponseWithTimeout(CompletableFuture<GenerateTokenResponse> future) {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            fail("Token generation did not complete within expected time: " + e.getMessage());
            return null;
        }
    }
}