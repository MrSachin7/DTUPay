package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class UnregisterCustomerRequested implements Serializable {

    private String coRelationId;
    private String customerId;


    // Ensure backward compatibility with serialVersionUID

    // Default constructor
    public UnregisterCustomerRequested() {
    }

    // Parameterized constructor
    public UnregisterCustomerRequested(String coRelationId, String customerId) {
        this.coRelationId = coRelationId;
        this.customerId = customerId;

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
