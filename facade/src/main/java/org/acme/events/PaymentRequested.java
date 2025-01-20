package org.acme.events;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PaymentRequested {
    private String correlationId;
    private String token;
    private double amount;

    private String merchantId;

    public PaymentRequested() {
    }

    public PaymentRequested(String correlationId, String token,  String merchantId, double amount) {
        this.correlationId = correlationId;
        this.token = token;
        this.amount = amount;
        this.merchantId = merchantId;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}
