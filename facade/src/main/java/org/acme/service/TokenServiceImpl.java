package org.acme.service;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.GenerateTokenResponse;
import org.acme.events.GenerateTokenCompleted;
import org.acme.events.GenerateTokenRequested;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class TokenServiceImpl implements TokenService {

    private final Emitter<GenerateTokenRequested> tokenRequestEmitter;

    private final ConcurrentHashMap<String, CompletableFuture<List<String>>> coRelations = new ConcurrentHashMap<>();

    public TokenServiceImpl(@Channel("GenerateTokenRequested") Emitter<GenerateTokenRequested> tokenRequestEmitter) {
        this.tokenRequestEmitter = tokenRequestEmitter;
    }


    @Override
    public GenerateTokenResponse generateToken(String username, int amount) {
       GenerateTokenRequested event = new GenerateTokenRequested(UUID.randomUUID().toString(),
               username, amount);

       CompletableFuture<List<String>> futureResponse = new CompletableFuture<>();
       coRelations.put(event.getCoRelationId(), futureResponse);

       try{
              System.out.println("Sending request to generate token");
              tokenRequestEmitter.send(event);

              List<String> tokens = futureResponse.get(30, TimeUnit.SECONDS);
              return new GenerateTokenResponse(tokens);
         } catch (Exception e) {
              coRelations.remove(event.getCoRelationId());
              throw new RuntimeException(e.getMessage());
       }
    }
    @Incoming("GenerateTokenCompleted")
    public void process(JsonObject response){
        GenerateTokenCompleted event = response.mapTo(GenerateTokenCompleted.class);
        CompletableFuture<List<String>> future = coRelations.remove(event.getCoRelationId());

        if (event.wasSuccessful()){
            future.complete(event.getTokens());
            return;
        }
        future.completeExceptionally(new RuntimeException(event.getError()));
    }
}
