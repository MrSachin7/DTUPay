package events;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class UnregisterCustomerCompleted implements Serializable {

    private String coRelationId;

    private String error;


    // Default constructor
    public UnregisterCustomerCompleted() {}

    // Parameterized constructor
    public UnregisterCustomerCompleted(String coRelationId, String error) {
        this.coRelationId = coRelationId;
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
}
