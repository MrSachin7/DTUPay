package core.domain.payment;

import core.domain.common.Aggregate;

public class Payment extends Aggregate<PaymentId> {
    private Amount amount;
    private BankAccountNumber CustomerBankAccount;
    private BankAccountNumber MerchantBankAccount;
    private MerchantId merchantId;
    private CustomerId customerId;
    private Token token;

    private Payment() {
    }

    public static Payment newPayment(){
        Payment payment = new Payment();
        payment.id = PaymentId.newPaymentId();
        return payment;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public void setCustomerBankAccount(BankAccountNumber customerBankAccount) {
        this.CustomerBankAccount = customerBankAccount;
    }

    public void setMerchantBankAccount(BankAccountNumber merchantBankAccount) {
        this.MerchantBankAccount = merchantBankAccount;
    }

    public BankAccountNumber getCustomerBankAccount() {
        return CustomerBankAccount;
    }

    public BankAccountNumber getMerchantBankAccount() {
        return MerchantBankAccount;
    }

    public Amount getAmount() {
        return amount;
    }

    public MerchantId getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(MerchantId merchantId) {
        this.merchantId = merchantId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
