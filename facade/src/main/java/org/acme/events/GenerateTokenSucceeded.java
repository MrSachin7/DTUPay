package org.acme.events;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class GenerateTokenSucceeded {

    private String coRelationId;
    private List<String> tokens;

    // Default constructor
    public GenerateTokenSucceeded() {}

    // Parameterized constructor
    public GenerateTokenSucceeded(String coRelationId, List<String> tokens) {
        this.coRelationId = coRelationId;
        this.tokens = tokens;
    }

    // Getters and Setters
    public String getCoRelationId() {
        return coRelationId;
    }

    public void setCoRelationId(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
