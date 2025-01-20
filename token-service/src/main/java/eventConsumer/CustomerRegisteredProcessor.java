package eventConsumer;

import core.domainService.CustomerService;
import events.RegisterCustomerCompleted;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class CustomerRegisteredProcessor{

    private final CustomerService customerService;
    public CustomerRegisteredProcessor(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Incoming("RegisterCustomerCompleted")
    public void process(JsonObject request) {
        RegisterCustomerCompleted event = request.mapTo(RegisterCustomerCompleted.class);
        if (!event.wasSuccessful()) return;
        String customerId = event.getCustomerId();
        System.out.println("Received response for customer registration"+ customerId);
        customerService.addCustomer(customerId);
    }
}
