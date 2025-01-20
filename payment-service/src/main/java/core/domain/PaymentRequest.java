package core.domain;

public class PaymentRequest {
    private String correlationId;
    private String paymentId;
    private String customerId;
    private double amount;


    public PaymentRequest() {}

    public PaymentRequest(String paymentId, String customerId, double amount) {
        this.correlationId = correlationId;
        this.paymentId = paymentId;
        this.customerId = customerId;
        this.amount = amount;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
