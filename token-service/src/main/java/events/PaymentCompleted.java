package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PaymentCompleted {

    private String coRelationId;
    private String customerId;
    private String tokenUsed;

    private String error = null;


    // Default constructor
    public PaymentCompleted() {}

    public PaymentCompleted(String coRelationId, String customerId, String tokenUsed) {
        this.coRelationId = coRelationId;
        this.customerId = customerId;
        this.tokenUsed = tokenUsed;
    }

    public PaymentCompleted(String coRelationId, String customerId, String tokenUsed, String error) {
        this.coRelationId = coRelationId;
        this.customerId = customerId;
        this.tokenUsed = tokenUsed;
        this.error = error;
    }

    public boolean wasSuccessful(){
        return error == null;
    }

    public String getCoRelationId() {
        return coRelationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTokenUsed() {
        return tokenUsed;
    }

    public void setCoRelationId(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public void setTokenUsed(String tokenUsed) {
        this.tokenUsed = tokenUsed;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
