package events;
import java.io.Serializable;

public class RegisterCustomerCompleted implements Serializable {

    private String coRelationId;
    private String customerId;

    private String error;


    // Default constructor
    public RegisterCustomerCompleted() {}

    // Parameterized constructor
    public RegisterCustomerCompleted(String coRelationId, String customerId, String error) {
        this.coRelationId = coRelationId;
        this.customerId = customerId;
        this.error = error;
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
