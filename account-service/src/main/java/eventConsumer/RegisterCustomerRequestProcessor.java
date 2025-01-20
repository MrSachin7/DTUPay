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

@ApplicationScoped
public class RegisterCustomerRequestProcessor {

    private final AccountService accountService;

    @Inject
    @Channel("RegisterCustomerCompleted")
    Emitter<RegisterCustomerCompleted> registerCustomerCompletedEmitter;

    public RegisterCustomerRequestProcessor(AccountService accountService) {
        this.accountService = accountService;
    }

    @Incoming("RegisterCustomerRequested")
    public void process(JsonObject obj){
        System.out.println("Processing request to register customer");
        RegisterCustomerRequested event = obj.mapTo(RegisterCustomerRequested.class);
        try {
            String id = accountService.registerAccount(event.getFirstname(), event.getLastname(), event.getCprNumber(), event.getAccountNumber());
            System.out.println("Customer registered with id: " + id);
            registerCustomerCompletedEmitter.send(new RegisterCustomerCompleted(event.getCoRelationId(),id, null));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            registerCustomerCompletedEmitter.send(new RegisterCustomerCompleted(event.getCoRelationId(),null, e.getMessage()));
        }
    }

}
