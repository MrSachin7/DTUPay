package org.acme.events;

public class PaymentRequested {
    private String correlationId;
    private String token;
    private String merchantId;
    private double amount;

    public PaymentRequested() {}

    public PaymentRequested(String correlationId, String token, String merchantId, double amount) {
        this.correlationId = correlationId;
        this.token = token;
        this.merchantId = merchantId;
        this.amount = amount;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentRequested{" +
                "correlationId='" + correlationId + '\'' +
                ", token='" + token + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
