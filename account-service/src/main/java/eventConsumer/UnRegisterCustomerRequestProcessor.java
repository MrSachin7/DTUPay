package eventConsumer;


import core.domainService.AccountService;
import events.RegisterCustomerCompleted;
import events.RegisterCustomerRequested;
import events.UnregisterCustomerCompleted;
import events.UnregisterCustomerRequested;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class UnRegisterCustomerRequestProcessor {

    private final AccountService accountService;

    private final Emitter<UnregisterCustomerCompleted> unregisterCustomerCompletedEmitter;

    public UnRegisterCustomerRequestProcessor(AccountService accountService, @Channel("UnregisterCustomerCompleted") Emitter<UnregisterCustomerCompleted> unregisterCustomerCompletedEmitter) {
        this.accountService = accountService;
        this.unregisterCustomerCompletedEmitter = unregisterCustomerCompletedEmitter;
    }

    @Incoming("UnregisterCustomerRequested")
    public void process(JsonObject obj){
        UnregisterCustomerRequested event = obj.mapTo(UnregisterCustomerRequested.class);
        try {
             accountService.unregisterAccount(event.getCustomerId());
            unregisterCustomerCompletedEmitter.send(new UnregisterCustomerCompleted(event.getCoRelationId(),null));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            unregisterCustomerCompletedEmitter.send(new UnregisterCustomerCompleted(event.getCoRelationId(), e.getMessage()));
        }
    }

}
