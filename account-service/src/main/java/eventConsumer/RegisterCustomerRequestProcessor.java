package eventConsumer;


import core.domainService.CustomerService;
import events.RegisterCustomerRequested;
import events.RegisterCustomerSucceeded;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class RegisterCustomerRequestProcessor {

    private final CustomerService customerService;

    @Inject
    @Channel("RegisterCustomerSucceeded")
    Emitter<RegisterCustomerSucceeded> customerSucceededEmitter;

    public RegisterCustomerRequestProcessor(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Incoming("RegisterCustomerRequested")
    public void process(JsonObject obj){
        System.out.println("Processing request to register customer");
        RegisterCustomerRequested event = obj.mapTo(RegisterCustomerRequested.class);
        String id = customerService.registerCustomer(event.getFirstname(), event.getLastname(), event.getCprNumber(), event.getAccountNumber());
        System.out.println("Customer registered with id: " + id);
        customerSucceededEmitter.send(new RegisterCustomerSucceeded(event.getCoRelationId(),id));
    }

}
