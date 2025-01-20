//package cucumbertests;
//
//import core.domain.token.Customer;
//import core.domain.token.CustomerId;
//import core.domain.token.CustomerRepository;
//import core.domain.token.TokenException;
//import core.domainService.TokenService;
//import events.PaymentCompleted;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.vertx.core.json.JsonObject;
//import persistence.CustomerRepositoryImpl;
//
//import java.util.UUID;
//
//import static org.junit.Assert.*;
//
//
//public class PaymentCompletedSteps {
//
//    private final CustomerRepository repository;
//
//    private final Customer existingCustomer = Customer.from(CustomerId.newCustomerId());
//
//    private final PaymentCompletedProcessor processor;
//
//    private final String coRelationId = UUID.randomUUID().toString();
//
//    private String tokenToUse;
//
//    public PaymentCompletedSteps() {
//        this.repository = new CustomerRepositoryImpl();
//        processor = new PaymentCompletedProcessor(new TokenService(repository));
//    }
//
//    @Given("a customer exists in the system")
//    public void aCustomerExistsInTheSystem() {
//        repository.add(existingCustomer);
//    }
//
//    @And("the customer already has a token")
//    public void theCustomerAlreadyHasAToken() throws TokenException {
//        existingCustomer.generateTokens(5);
//        tokenToUse = existingCustomer.getTokens().getFirst();
//
//        assertEquals(5, existingCustomer.getTokens().size());
//    }
//
//    @When("the system recieves the payment completed event with successful payment")
//    public void theSystemRecievesThePaymentCompletedEventWithSuccessfulPayment() {
//        PaymentCompleted event = new PaymentCompleted(coRelationId,
//                existingCustomer.getId().getValue(),
//                tokenToUse);
//
//        JsonObject request = JsonObject.mapFrom(event);
//
//        processor.process(request);
//
//    }
//
//    @When("the system recieves the payment completed event with unsuccessful payment")
//    public void theSystemRecievesThePaymentCompletedEventWithUnsuccessfulPayment() {
//        // Payment with error
//        PaymentCompleted event = new PaymentCompleted(coRelationId,
//                existingCustomer.getId().getValue(),
//                tokenToUse,
//                "Payment failed");
//
//        JsonObject request = JsonObject.mapFrom(event);
//        processor.process(request);
//    }
//
//    @Then("the system removes the used token from the customer")
//    public void theSystemRemovesTheUsedTokenFromTheCustomer() {
//        // Token is removed
//        assertEquals(4, existingCustomer.getTokens().size());
//
//        // Token is not present anymore
//        assertTrue(existingCustomer.getTokens().stream().noneMatch(token -> token.equals(tokenToUse)));
//    }
//
//
//    @Then("the system does not remove the used token from the customer")
//    public void theSystemDoesNotRemoveTheUsedTokenFromTheCustomer() {
//
//        assertEquals(5, existingCustomer.getTokens().size());
//
//        // Token is still present since its not used
//        assertTrue(existingCustomer.getTokens().stream().anyMatch(token -> token.equals(tokenToUse)));
//    }
//
//
//}
