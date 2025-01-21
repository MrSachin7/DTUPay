package eventConsumer;


import core.domainService.AccountService;
import events.RegisterCustomerCompleted;
import events.RegisterCustomerRequested;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class RegisterCustomerRequestProcessor {

    private final AccountService accountService;

    public RegisterCustomerRequestProcessor(AccountService accountService) {
        this.accountService = accountService;
    }

    @Incoming("RegisterCustomerRequested")
    @Outgoing("RegisterCustomerCompleted")
    public RegisterCustomerCompleted process(JsonObject obj){
        System.out.println("Processing request to register customer");
        RegisterCustomerRequested event = obj.mapTo(RegisterCustomerRequested.class);
        try {
            String id = accountService.registerAccount(event.getFirstname(), event.getLastname(), event.getCprNumber(), event.getAccountNumber());
            System.out.println("Customer registered with id: " + id);
            return new RegisterCustomerCompleted(event.getCoRelationId(),id, null);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new RegisterCustomerCompleted(event.getCoRelationId(),null, e.getMessage());
        }
    }

}
