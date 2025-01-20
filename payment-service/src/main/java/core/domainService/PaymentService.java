package core.domainService;

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

    public void processPayment(Payment payment) throws BankServiceException_Exception {


        bankService.transferMoneyFromTo(payment.getMerchantAccount().getValue(),
                payment.getCustomerAccount().getValue(),
                BigDecimal.valueOf(payment.getAmount().getValue()),
                "");
    }
}
