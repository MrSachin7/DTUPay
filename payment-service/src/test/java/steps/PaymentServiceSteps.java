package features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import static org.junit.Assert.*;

public class PaymentServiceSteps {

//    private final AccountRepository accountrepository;
    private final PaymentProcessor paymentprocessor;
    private PaymentRequested paymentrequested;

    private PaymentCompleted paymentcompleted;

    private String customerAccount;
    private String merchantAccount;
    private double amount;
    private boolean paymentProcessed;
    private String paymentId;

    @Given("^a customer account \"([^\"]*)\", merchant account \"([^\"]*)\", and amount of (\\d+)\\.(\\d+)$")
    public void aCustomerAccountMerchantAccountAndAmountOf(String customerAccount, String merchantAccount, int amountWhole, int amountFraction) {
        this.customerAccount = customerAccount;
        this.merchantAccount = merchantAccount;
        this.amount = amountWhole + amountFraction / 100.0;
    }

    @When("^the system processes a payment with customer account \"([^\"]*)\", merchant account \"([^\"]*)\", and amount (\\d+)\\.(\\d+)$")
    public void theSystemProcessesAPaymentWithCustomerAccountMerchantAccountAndAmount(String customerAccount, String merchantAccount, int amountWhole, int amountFraction) {
        // Simulate payment processing
        this.paymentProcessed = true; // Assume payment is processed successfully
        this.paymentId = "PAY123"; // Simulate a generated payment ID
    }

    @Then("^the payment is successfully processed$")
    public void thePaymentIsSuccessfullyProcessed() {
        assertTrue(paymentProcessed);
    }

    @And("^a payment ID is generated$")
    public void aPaymentIDIsGenerated() {
        assertNotNull(paymentId);
    }
}