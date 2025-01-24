package eventConsumer;


import core.domainService.AccountService;
import events.UnregisterCustomerCompleted;
import events.UnregisterCustomerRequested;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class UnRegisterCustomerRequestProcessor {

    private final AccountService accountService;


    public UnRegisterCustomerRequestProcessor(AccountService accountService) {
        this.accountService = accountService;
    }

    @Incoming("UnregisterCustomerRequested")
    @Outgoing("UnregisterCustomerCompleted")
    public UnregisterCustomerCompleted process(JsonObject obj){
        UnregisterCustomerRequested event = obj.mapTo(UnregisterCustomerRequested.class);
        System.out.println("UnregisterCustomerRequested event received"+ event.getCustomerId());
        try {
             accountService.unregisterAccount(event.getCustomerId());
            System.out.println("UnregisterCustomerCompleted event sent with success"+ event.getCoRelationId());

            return (new UnregisterCustomerCompleted(event.getCoRelationId(),event.getCustomerId(),null));

        } catch (Exception e) {
            System.out.println("UnregisterCustomerCompleted event sent with failure"+ event.getCoRelationId());
            return (new UnregisterCustomerCompleted(event.getCoRelationId(), null, e.getMessage()));
        }
    }

}
