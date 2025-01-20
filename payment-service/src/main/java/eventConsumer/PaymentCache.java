package eventConsumer;

import core.domain.payment.Payment;

public class PaymentCache {

    private Payment payment;

    private String error;


    public PaymentCache() {
        this.payment = Payment.newPayment();
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getError() {
        return error;
    }

    public Payment getPayment() {
        return payment;
    }

    public boolean wasSuccessful() {
        return error == null;
    }
}
