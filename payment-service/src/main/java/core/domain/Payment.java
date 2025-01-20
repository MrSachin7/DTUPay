package core.domain;

public class Payment {
    private final String paymentId;
    private final String customerId;

    private final double amount;
    private String status;

    public Payment(String paymentId, String customerId, double amount) {
        this.paymentId = paymentId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = "PENDING";
    }

    public void complete() {
        this.status = "COMPLETED";
    }

    public void fail() {
        this.status = "FAILED";
    }

    public String getStatus() {
        return status;
    }

    public double getAmount() {
        return amount;
    }
}
