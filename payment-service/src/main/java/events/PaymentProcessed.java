package events;

public class PaymentProcessed {
    private String tokenId;
    private String merchantId;
    private double amount;

    public PaymentProcessed() {
    }

    public PaymentProcessed(String tokenId, String merchantId, double amount) {
        this.tokenId = tokenId;
        this.merchantId = merchantId;
        this.amount = amount;
    }

    // Getters and setters
    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
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
}
