package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AccountValidationCompleted {
    private String correlationId;
    private String userId;

    private String bankAccountNumber;

    private String error = null;

    public AccountValidationCompleted() {
    }

    public AccountValidationCompleted(String correlationId, String userId, String bankAccountNumber, String error) {
        this.correlationId = correlationId;
        this.userId = userId;
        this.bankAccountNumber = bankAccountNumber;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean wasSuccessful() {
        return error == null;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }


}
