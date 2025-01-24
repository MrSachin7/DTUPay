package cucumbertests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import org.acme.events.UnregisterCustomerRequested;
import org.acme.events.UnregisterCustomerCompleted;
import org.acme.service.UnregisterService;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerOrMerchantUnregistrationSteps {
    private String customerId1;
    private UnregisterCustomerRequested unregisterRequested;
    private final UnregisterService unregisterService;
    private final Emitter<UnregisterCustomerRequested> emitter;


    public CustomerOrMerchantUnregistrationSteps() {
        emitter = mock(Emitter.class);
        unregisterService = new UnregisterService(emitter);

        when(emitter.send(any(UnregisterCustomerRequested.class))).thenAnswer(invocation -> {
            UnregisterCustomerRequested event = invocation.getArgument(0);
            UnregisterCustomerCompleted completed = new UnregisterCustomerCompleted(
                    event.getCoRelationId(),
                    event.getCustomerId()
            );

            unregisterService.process(JsonObject.mapFrom(completed));

            return null;
        });
    }

    @Given("a customer")
    public void aCustomer() {
        customerId1 = UUID.randomUUID().toString();
    }

    @And("the customer is registered with DTUPay")
    public void theCustomerIsRegisteredWithDTUPay() {
        assertNotNull(customerId1);
    }

    @When("the customer unregisters with DTUPay")
    public void theCustomerUnregistersWithDTUPay() {
        unregisterRequested = new UnregisterCustomerRequested(UUID.randomUUID().toString(), customerId1);
        unregisterService.unregisterCustomer(customerId1);
    }

    @Then("the UnregisterCustomerRequested event is published")
    public void theUnregisterCustomerRequestedEventIsPublished() {
        verify(emitter, times(1)).send(any(UnregisterCustomerRequested.class));
    }

    @And("the UnregisterCustomerRequested contains the correct data")
    public void theUnregisterCustomerRequestedContainsTheCorrectData() {
        verify(emitter, times(1)).send((UnregisterCustomerRequested) argThat(event -> {
            UnregisterCustomerRequested firedEvent = (UnregisterCustomerRequested) event;
            return firedEvent.getCustomerId().equals(unregisterRequested.getCustomerId());
        }));
    }
}
