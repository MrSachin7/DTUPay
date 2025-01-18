package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class GenerateTokenCompleted {
    private String coRelationId;
    private String error = null;

    private List<String> tokens = new ArrayList<>(6);

    public GenerateTokenCompleted() {}

    public GenerateTokenCompleted(String coRelationId, String error) {
        this.coRelationId = coRelationId;
        this.error = error;
    }

    public GenerateTokenCompleted(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public GenerateTokenCompleted(String coRelationId, List<String> tokens) {
        this.coRelationId = coRelationId;
        this.tokens = tokens;
    }


    public boolean wasSuccessful(){
        return error == null;
    }

    public String getCoRelationId() {
        return coRelationId;
    }

    public String getError() {
        return error;
    }

    public void setCoRelationId(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
