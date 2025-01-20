package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ValidateTokenCompleted {
    private String correlationId;

    private String customerId;
    private String error = null;

    public ValidateTokenCompleted() {}

    public ValidateTokenCompleted(String correlationId, String customerId, String error) {
        this.correlationId = correlationId;
        this.error = error;
        this.customerId = customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public ValidateTokenCompleted(String coRelationId) {
        this.correlationId = coRelationId;
    }

    public boolean wasSuccessful(){
        return error == null;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getError() {
        return error;
    }
}
