package core.domain.payment;

import core.domain.common.Aggregate;

public class Payment extends Aggregate<PaymentId> {
    private Amount amount;
    private BankAccountNumber customerAccount;
    private BankAccountNumber merchantAccount;

    private Payment(){

    }

    public static Payment newPayment(){
        Payment payment = new Payment();
        payment.id = PaymentId.newPaymentId();
        return payment;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public void setCustomerAccount(BankAccountNumber customerAccount) {
        this.customerAccount = customerAccount;
    }

    public void setMerchantAccount(BankAccountNumber merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    public BankAccountNumber getCustomerAccount() {
        return customerAccount;
    }

    public BankAccountNumber getMerchantAccount() {
        return merchantAccount;
    }

    public Amount getAmount() {
        return amount;
    }
}
