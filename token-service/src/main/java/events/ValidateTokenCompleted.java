package events;

import io.quarkus.runtime.annotations.RegisterForReflection;
@RegisterForReflection
public class ValidateTokenCompleted {
    private String coRelationId;
    private String error = null;

    public ValidateTokenCompleted() {}

    public ValidateTokenCompleted(String coRelationId, String error) {
        this.coRelationId = coRelationId;
        this.error = error;
    }

    public ValidateTokenCompleted(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public boolean wasSuccessful(){
        return error == null;
    }

    public void setCoRelationId(String coRelationId) {
        this.coRelationId = coRelationId;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCoRelationId() {
        return coRelationId;
    }

    public String getError() {
        return error;
    }
}
