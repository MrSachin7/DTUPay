package eventConsumer;

import core.domainService.TokenService;
import events.ValidateTokenCompleted;
import events.ValidateTokenRequested;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class ValidateTokenRequestProcessor {
    private final TokenService tokenService;


    public ValidateTokenRequestProcessor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Incoming("ValidateTokenRequested")
    @Outgoing("ValidateTokenCompleted")
    public ValidateTokenCompleted process(JsonObject request){
        ValidateTokenRequested event = request.mapTo(ValidateTokenRequested.class);

        try{
            tokenService.validateToken(event.getCustomerId(), event.getToken());
            return new ValidateTokenCompleted(event.getCoRelationId());
        } catch (Exception e){
            return new ValidateTokenCompleted(event.getCoRelationId(), e.getMessage());
        }
    }
}
