package eventConsumer;

import core.domain.common.NotFoundException;
import core.domain.token.TokenException;
import core.domainService.TokenService;
import events.GenerateTokenCompleted;
import events.GenerateTokenRequested;
import jakarta.enterprise.context.ApplicationScoped;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.List;

@ApplicationScoped
public class GenerateTokenRequestProcessor {

    private final TokenService tokenService;

    public GenerateTokenRequestProcessor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Incoming("GenerateTokenRequested")
    @Outgoing("GenerateTokenCompleted")
    public GenerateTokenCompleted process(JsonObject request) {
        System.out.println("Processing token request");
        GenerateTokenRequested event = request.mapTo(GenerateTokenRequested.class);
        try {
            List<String> tokens = tokenService.generateTokens(event.getCustomerId(), event.getAmount());
            return new GenerateTokenCompleted(event.getCoRelationId(), tokens);
        } catch (TokenException | NotFoundException e) {
            return new GenerateTokenCompleted(event.getCoRelationId(), e.getMessage());
        }
    }
}
