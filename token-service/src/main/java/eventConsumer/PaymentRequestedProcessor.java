package eventConsumer;

import core.domain.token.Customer;
import core.domainService.TokenService;
import events.PaymentRequested;
import events.ValidateTokenCompleted;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class PaymentRequestedProcessor {
    private final TokenService tokenService;

    public PaymentRequestedProcessor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Incoming("PaymentRequested")
    @Outgoing("ValidateTokenCompleted")
    public ValidateTokenCompleted process(JsonObject request){
        PaymentRequested event = request.mapTo(PaymentRequested.class);
        System.out.println("PaymentRequested event received: " + event.getCorrelationId());

        try{
            String customerId = tokenService.validateToken(event.getToken());
            System.out.println("ValidateTokenCompleted event sent with success: " + event.getCorrelationId());
            return new ValidateTokenCompleted(event.getCorrelationId(), customerId, null);
        } catch (Exception e){
            System.out.println("ValidateTokenCompleted event sent with failure: " + event.getCorrelationId());
            return new ValidateTokenCompleted(event.getCorrelationId(), null, e.getMessage());
        }
    }
}
