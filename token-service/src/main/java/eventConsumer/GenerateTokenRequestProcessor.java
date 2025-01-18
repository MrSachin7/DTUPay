package eventConsumer;

import core.domainService.TokenService;
import events.GenerateTokenRequested;
import jakarta.enterprise.context.ApplicationScoped;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class GenerateTokenRequestProcessor {

    private final TokenService tokenService;

    public GenerateTokenRequestProcessor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public GenerateTokenRequested process(JsonObject request) {
        tokenService.generateToken(request.getCustomerId());
    }
}
