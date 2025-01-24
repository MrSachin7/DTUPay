package eventConsumer;


import core.domainService.AccountService;
import events.RegisterCustomerCompleted;
import events.RegisterCustomerRequested;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

/**
 * @author: Janusz Jakub Wilczek (s243891)
 */
@ApplicationScoped
public class RegisterCustomerRequestProcessor {

    private final AccountService accountService;


    public RegisterCustomerRequestProcessor(AccountService accountService) {
        this.accountService = accountService;
    }

    @Incoming("RegisterCustomerRequested")
    @Outgoing("RegisterCustomerCompleted")
    public RegisterCustomerCompleted process(JsonObject obj){
        RegisterCustomerRequested event = obj.mapTo(RegisterCustomerRequested.class);
        System.out.println("RegisterCustomerRequested event received " + event.getCoRelationId());
        try {
            String id = accountService.registerAccount(event.getFirstname(), event.getLastname(), event.getCprNumber(), event.getAccountNumber());
            System.out.println("RegisterCustomerCompleted event sent with success "+ event.getCoRelationId());

            return new RegisterCustomerCompleted(event.getCoRelationId(),id, null);

        } catch (Exception e) {
            System.out.println("RegisterCustomerCompleted event sent with failure "+ event.getCoRelationId());
            return new RegisterCustomerCompleted(event.getCoRelationId(), null, e.getMessage());
        }
    }
}
