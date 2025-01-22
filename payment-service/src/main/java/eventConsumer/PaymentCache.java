package eventConsumer;

import core.domain.payment.Payment;

public class PaymentCache {

    private double amount;

    private String customerAccount;

    private String merchantAccount;

    private String error;


    public PaymentCache() {

    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean wasSuccessful() {
        return error == null;
    }


    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
