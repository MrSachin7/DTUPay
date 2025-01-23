package features;

import core.domain.common.Repository;
import eventConsumer.PaymentProcessor;
import events.AccountValidationCompleted;
import events.PaymentCompleted;
import events.PaymentRequested;
import io.cucumber.java.en.*;
import io.vertx.core.json.JsonObject;
import persistance.RepositoryImpl

import static org.junit.Assert.*;

public class PaymentServiceSteps {

    // private final Repository accountrepository;   // Not strictly used in example
    private final AccountRepository accountRepository;
    private final PaymentProcessor paymentprocessor;

    private PaymentRequested paymentrequested;
    private PaymentCompleted paymentcompleted;

    private String customerAccount;
    private String merchantAccount;
    private double amount;

    private boolean paymentProcessed;
    private String paymentId;
    private String correlationId;

    public PaymentServiceSteps(
            Repository accountrepository,
            PaymentProcessor paymentprocessor) {
        this.accountrepository = new RepositoryImpl();
        this.paymentprocessor = paymentprocessor;
    }


    @Given("^a customer account \"([^\"]*)\", merchant account \"([^\"]*)\", and amount of (\\d+)\\.(\\d+)$")
    public void aCustomerAccountMerchantAccountAndAmountOf(String customerAccount,
                                                           String merchantAccount,
                                                           int amountWhole,
                                                           int amountFraction) {
        this.customerAccount = customerAccount;
        this.merchantAccount = merchantAccount;
        this.amount = amountWhole + amountFraction / 100.0;
    }

    @When("^the system processes a payment with customer account \"([^\"]*)\", merchant account \"([^\"]*)\", and amount (\\d+)\\.(\\d+)$")
    public void theSystemProcessesAPaymentWithCustomerAccountMerchantAccountAndAmount(String custAcc,
                                                                                      String merchAcc,
                                                                                      int amountWhole,
                                                                                      int amountFraction) {
        this.correlationId = "TEST-CORR-ID-" + System.currentTimeMillis();

        // Build and send the PaymentRequested event
        paymentrequested = new PaymentRequested(
                correlationId,
                "dummy-token",
                "dummy-merchantId",
                amountWhole + amountFraction / 100.0
        );

        paymentprocessor.processPaymentRequest(JsonObject.mapFrom(paymentrequested));

        AccountValidationCompleted merchantValidatedEvent = new AccountValidationCompleted(
                correlationId,
                "merchant-user-id",
                merchAcc,
                "error"
        );
        paymentprocessor.processMerchantValidation(JsonObject.mapFrom(merchantValidatedEvent));

        AccountValidationCompleted customerValidatedEvent = new AccountValidationCompleted(
                correlationId,
                "customer-user-id",
                custAcc,
                "error"
        );
        paymentprocessor.processCustomerValidation(JsonObject.mapFrom(customerValidatedEvent));

        paymentcompleted = paymentprocessor.getLastCompletedEvent();

        if (paymentcompleted != null) {
            this.paymentProcessed = paymentcompleted.wasSuccessful();
            this.paymentId = paymentcompleted.getPaymentId();
        } else {
            this.paymentProcessed = false;
            this.paymentId = null;
        }
    }

    @Then("^the payment is successfully processed$")
    public void thePaymentIsSuccessfullyProcessed() {
        assertTrue("Expected payment to be successful, but it failed or no event captured!",
                paymentProcessed);
    }

    @And("^a payment ID is generated$")
    public void aPaymentIDIsGenerated() {
        assertNotNull("Expected a non-null paymentId!", paymentId);
        assertFalse("Expected a non-empty paymentId!", paymentId.trim().isEmpty());
    }
}
