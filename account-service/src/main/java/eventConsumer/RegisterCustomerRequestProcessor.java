package eventConsumer;


import core.domainService.CustomerService;
import events.RegisterCustomerRequested;
import events.RegisterCustomerSucceeded;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class RegisterCustomerRequestProcessor {

    private final CustomerService customerService;

    public RegisterCustomerRequestProcessor(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Incoming("RegisterCustomerRequested")
    @Outgoing("RegisterCustomerSucceeded")
    public RegisterCustomerSucceeded process(JsonObject obj){
        System.out.println("Processing request to register customer");
        RegisterCustomerRequested event = obj.mapTo(RegisterCustomerRequested.class);
        String id = customerService.registerCustomer(event.getFirstname(), event.getLastname(), event.getCprNumber(), event.getAccountNumber());
        System.out.println("Customer registered with id: " + id);
        return new RegisterCustomerSucceeded(event.getCoRelationId(), id);
    }

}
