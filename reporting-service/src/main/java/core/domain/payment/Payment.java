package core.domain.payment;

import core.domain.common.Aggregate;

public class Payment extends Aggregate<PaymentId> {
    private Amount amount;
    private CustomerId customerId;
    private MerchantId merchantId;
    private Token token;

    private Payment() {
    }

    public static Payment from(PaymentId paymentId, Amount amount, CustomerId customerId, MerchantId merchantId,
            Token token) {
        Payment payment = new Payment();
        payment.id = paymentId;
        payment.amount = amount;
        payment.customerId = customerId;
        payment.merchantId = merchantId;
        payment.token = token;
        return payment;
    }

    public Amount getAmount() {
        return amount;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public MerchantId getMerchantId() {
        return merchantId;
    }

    public Token getToken() {
        return token;
    }
}
