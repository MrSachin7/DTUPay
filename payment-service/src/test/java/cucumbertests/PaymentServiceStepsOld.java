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
    private final BankService bankService;
    private final PaymentService paymentService;
    private String paymentId;

    public PaymentServiceSteps() {
        bankService = Mockito.mock(BankService.class);
        paymentService = new PaymentService(bankService);
    }

    @When("the system processes a payment with customer account {string}, merchant account {string}, and amount {double}")
    public void theSystemProcessesAPaymentWithCustomerAccountMerchantAccountAndAmount(String customerAccount, String merchantAccount, double amount) throws BankServiceException_Exception {
        paymentId = paymentService.processPayment(customerAccount, merchantAccount, amount);
    }

    @Then("the system stores the payment and transfers the money")
    public void theSystemStoresThePaymentAndTransfersTheMoney() {
        assertNotNull(paymentId);
        verify(bankService, times(1)).transferMoneyFromTo(
                "1234567890",
                "0987654321",
                BigDecimal.valueOf(50.0),
                "Dummy description"
        );
    }

    @When("the system processes a payment with an invalid amount {double}")
    public void theSystemProcessesAPaymentWithAnInvalidAmount(double amount) {
        Mockito.reset(bankService);
        Throwable thrown = org.junit.jupiter.api.Assertions.assertThrows(BankServiceException_Exception.class, () -> {
            paymentService.processPayment("1234567890", "0987654321", amount);
        });
    }

    @Then("the system does not transfer the money")
    public void theSystemDoesNotTransferTheMoney() {
        verify(bankService, times(0)).transferMoneyFromTo(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(BigDecimal.class),
                Mockito.anyString()
        );
    }

    private String customerAccount;
    private String merchantAccount;
    private double amount;
    private String paymentId;
    private PaymentService paymentService;
    private BankService bankService;

    @Given("^a customer account \"([^\"]*)\"$")
    public void aCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    @And("^a merchant account \"([^\"]*)\"$")
    public void aMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    @And("^an amount of (\\d+)\\.(\\d+)$")
    public void anAmountOf(int wholePart, int decimalPart) {
        this.amount = Double.parseDouble(wholePart + "." + decimalPart);
    }

    @When("^the system processes a payment with customer account \"([^\"]*)\", merchant account \"([^\"]*)\", and amount (\\d+)\\.(\\d+)$")
    public void theSystemProcessesAPaymentWithCustomerAccountMerchantAccountAndAmount(String customerAccount, String merchantAccount, int wholePart, int decimalPart) throws BankServiceException_Exception {
        double amount = Double.parseDouble(wholePart + "." + decimalPart);
        paymentId = paymentService.processPayment(customerAccount, merchantAccount, amount);
    }

    @Then("^the payment is successfully processed$")
    public void thePaymentIsSuccessfullyProcessed() {
        assertNotNull(paymentId);
    }

    @And("^a payment ID is generated$")
    public void aPaymentIDIsGenerated() {
        assertTrue(paymentId != null && !paymentId.isEmpty());
    }

    @And("^a transfer amount of (\\d+)\\.(\\d+)$")
    public void aTransferAmountOf(int wholePart, int decimalPart) {
        this.amount = Double.parseDouble(wholePart + "." + decimalPart);
    }

    @When("^the system processes a payment$")
    public void theSystemProcessesAPayment() throws BankServiceException_Exception {
        paymentId = paymentService.processPayment(customerAccount, merchantAccount, amount);
    }

    @Then("^the system stores the payment$")
    public void theSystemStoresThePayment() {
        assertNotNull(paymentId);
    }

    @And("^transfers money from \"([^\"]*)\" to \"([^\"]*)\" with amount (\\d+)\\.(\\d+)$")
    public void transfersMoneyFromToWithAmount(String fromAccount, String toAccount, int wholePart, int decimalPart) {
        verify(bankService, times(1)).transferMoneyFromTo(
                fromAccount,
                toAccount,
                BigDecimal.valueOf(Double.parseDouble(wholePart + "." + decimalPart)),
                "Dummy description"
        );
    }

}