package cucumbertests;

import core.domain.payment.Amount;
import core.domain.payment.Payment;
import core.domainService.PaymentService;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PaymentServiceSteps {
    @When("the system processes a payment with customer account {string}, merchant account {string}, and amount {double}")
    public void aCustomerAccountMerchantAccountAndAmountOf(String customerAccount, String merchantAccount, double amount) throws Throwable {
        paymentId = paymentService.processPayment(customerAccount, merchantAccount, amount);
        throw new cucumber.api.PendingException();
    }
}
