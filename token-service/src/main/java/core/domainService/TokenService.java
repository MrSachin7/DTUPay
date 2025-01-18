package core.domainService;

import events.GenerateTokenRequested;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {


    public void process(JsonObject object){
        System.out.println("Processing token request");

        GenerateTokenRequested event = object.mapTo(GenerateTokenRequested.class);
    }
}
