package eventConsumer;

import core.domainService.TokenService;
import events.UnregisterCustomerCompleted;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class UnregisterCustomerProcessor {

    private final TokenService tokenService;


    public UnregisterCustomerProcessor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Incoming("UnregisterCustomerCompleted")
    public void process(JsonObject jsonObject){
        try {
            UnregisterCustomerCompleted event = jsonObject.mapTo(UnregisterCustomerCompleted.class);
            System.out.println("UnregisterCustomerCompleted event received: " + event.getCustomerId());
            if (!event.wasSuccessful()){
                System.out.println("Ignoring failed unregister customer event");
                return;
            };
            tokenService.unregisterCustomer(event.getCustomerId());
        } catch (Exception e) {
            System.out.println("Failed to unregister customer: " + e.getMessage());
        }
    }
}
