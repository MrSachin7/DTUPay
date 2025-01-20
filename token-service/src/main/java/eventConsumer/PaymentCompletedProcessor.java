package eventConsumer;

import core.domainService.TokenService;
import events.PaymentCompleted;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PaymentCompletedProcessor {

    private final TokenService tokenService;

    public PaymentCompletedProcessor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Incoming("PaymentCompleted")
    public void process(JsonObject request) {
        PaymentCompleted event = request.mapTo(PaymentCompleted.class);

        // If the payment was not successful, do nothing
        if (!event.wasSuccessful()) return;

        tokenService.removeToken(event.getCustomerId(), event.getTokenUsed());
    }
}
