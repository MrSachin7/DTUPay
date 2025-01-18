package org.acme.events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class GenerateTokenRequested {

    private String coRelationId;
    private String customerId;
    private int amount;

    // Default constructor
    public GenerateTokenRequested() {}

    // Parameterized constructor

    public GenerateTokenRequested(String coRelationId, String customerId, int amount) {
        this.coRelationId = coRelationId;
        this.customerId = customerId;
        this.amount = amount;
    }

    // Getters and Setters
    public String getCoRelationId() {
        return coRelationId;
    }

    public void setCoRelationId(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
