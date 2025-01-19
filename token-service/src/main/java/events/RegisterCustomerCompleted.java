package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class RegisterCustomerCompleted implements Serializable {
    private String coRelationId;
    private String customerId;
    private String error = null;
    // Default constructor
    public RegisterCustomerCompleted() {}

    // Parameterized constructor
    public RegisterCustomerCompleted(String coRelationId, String customerId) {
        this.coRelationId = coRelationId;
        this.customerId = customerId;
    }

    public RegisterCustomerCompleted(String coRelationId, String customerId, String error) {
        this.coRelationId = coRelationId;
        this.customerId = customerId;
        this.error = error;
    }

    public boolean wasSuccessful(){
        return error == null;
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
}
