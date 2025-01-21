package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PaymentCompleted {

    private String correlationId;
    private String error;

    private String paymentId;

    public PaymentCompleted() {
    }

    public PaymentCompleted(String correlationId, String paymentId, String error) {
        this.correlationId = correlationId;
        this.error = error;
        this.paymentId = paymentId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
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

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
