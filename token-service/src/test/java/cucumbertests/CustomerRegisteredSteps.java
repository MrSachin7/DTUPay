package cucumbertests;

import core.domain.token.Customer;
import core.domain.token.CustomerId;
import core.domain.token.CustomerRepository;
import core.domainService.CustomerService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import persistence.CustomerRepositoryImpl;

import java.util.UUID;
import static org.junit.Assert.*;

public class CustomerRegisteredSteps {

    private final CustomerRepository repository;
    private final CustomerRegisteredProcessor processor;

    private JsonObject request;


    public CustomerRegisteredSteps() {
        this.repository = new CustomerRepositoryImpl();
        this.processor = new CustomerRegisteredProcessor(new CustomerService(repository));
    }

    @When("a customer registered event is received")
    public void aCustomerRegisteredEventIsReceived() {
        String customerId = UUID.randomUUID().toString();
        request = new JsonObject().put("customerId", customerId).put("coRelationId", UUID.randomUUID().toString());
        processor.process(request);
    }

    @Then("the customer should be registered to the repository")
    public void theCustomerShouldBeRegisteredToTheRepository() {
        Customer customer = repository.find(CustomerId.from(request.getString("customerId")));
        assertNotNull(customer);
        assertEquals(request.getString("customerId"), customer.getId().getValue());
    }



}
