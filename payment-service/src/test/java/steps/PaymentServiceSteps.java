public class PaymentServiceSteps {
    @io.cucumber.java.en.Given("^a customer account 1234, merchant account 5432, and amount of 150.00")
    public void aCustomerAccountMerchantAccountAndAmountOf(String customerId, String merchantId, double amount) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @io.cucumber.java.en.When("^the system processes a payment with customer account \"([^\"]*)\", merchant account \"([^\"]*)\", and amount (\\d+)\\.(\\d+)$")
    public void theSystemProcessesAPaymentWithCustomerAccountMerchantAccountAndAmount(String arg0, String arg1, int arg2, int arg3) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @io.cucumber.java.en.Then("^the payment is successfully processed$")
    public void thePaymentIsSuccessfullyProcessed() {
    }

    @io.cucumber.java.en.And("^a payment ID is generated$")
    public void aPaymentIDIsGenerated() {
    }
}
