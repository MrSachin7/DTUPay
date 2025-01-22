package events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PaymentCompleted {

    private String correlationId;
    private String error;
    private String paymentId;
    private String token;
    private double amount;
    private String merchantId;
    private String customerId;

    public PaymentCompleted() {
    }

    public PaymentCompleted(String correlationId, String error, String paymentId, String token, double amount, String merchantId, String customerId) {
        this.correlationId = correlationId;
        this.error = error;
        this.paymentId = paymentId;
        this.token = token;
        this.amount = amount;
        this.merchantId = merchantId;
        this.customerId = customerId;
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

    public String getToken() {
        return token;
    }

    public double getAmount() {
        return amount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getCustomerId() {
        return customerId;
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
