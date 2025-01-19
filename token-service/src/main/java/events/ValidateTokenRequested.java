package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ValidateTokenRequested {

    private String coRelationId;
    private String customerId;

    private String token;


    // Default constructor
    public ValidateTokenRequested() {}

    public ValidateTokenRequested(String coRelationId, String customerId, String token) {
        this.coRelationId = coRelationId;
        this.customerId = customerId;
        this.token = token;
    }

    public String getCoRelationId() {
        return coRelationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getToken() {
        return token;
    }

    public void setCoRelationId(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}

