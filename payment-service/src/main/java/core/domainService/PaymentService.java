package core.domainService;

import core.domain.payment.Amount;
import core.domain.payment.BankAccountNumber;
import core.domain.payment.Payment;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

@ApplicationScoped
public class PaymentService {

    private final BankService bankService;

    public PaymentService(BankService bankService) {
        this.bankService = bankService;
    }
    
    public String processPayment(String customerAccount, String merchantAccount, double amount) throws BankServiceException_Exception {

        Payment payment = Payment.newPayment();
        payment.setAmount(Amount.from(amount));
        payment.setCustomerBankAccount(BankAccountNumber.from(customerAccount));
        payment.setMerchantBankAccount(BankAccountNumber.from(merchantAccount));


        System.out.println("Transferring money from " + payment.getCustomerBankAccount().getValue() + " to " + payment.getMerchantBankAccount().getValue());

        bankService.transferMoneyFromTo(payment.getCustomerBankAccount().getValue(),
                payment.getMerchantBankAccount().getValue(),
                BigDecimal.valueOf(payment.getAmount().getValue()),
                "Dummy description");

        return payment.getId().getValue();
    }
}
