package events;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class UnregisterCustomerCompleted implements Serializable {

    private String coRelationId;

    private String customerId;

    private String error;


    // Default constructor
    public UnregisterCustomerCompleted() {}

    // Parameterized constructor
    public UnregisterCustomerCompleted(String coRelationId, String customerID, String error) {
        this.coRelationId = coRelationId;
        this.customerId = customerID;
        this.error = error;
    }

    // Getters and Setters
    public String getCoRelationId() {
        return coRelationId;
    }

    public void setCoRelationId(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public boolean wasSuccessful(){
        return error == null;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }


}
