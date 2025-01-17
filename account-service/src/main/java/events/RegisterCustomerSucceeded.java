package events;
import java.io.Serializable;

public class RegisterCustomerSucceeded implements Serializable {

    private String coRelationId;
    private String customerId;


    // Default constructor
    public RegisterCustomerSucceeded() {}

    // Parameterized constructor
    public RegisterCustomerSucceeded(String coRelationId, String customerId) {
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
