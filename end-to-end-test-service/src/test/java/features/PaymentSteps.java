package features;

import dto.GenerateTokenResponse;
import dto.RegisterCustomerRequest;
import dto.RegisterCustomerResponse;
import dto.StartPaymentRequest;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import services.CustomerService;
import services.MerchantService;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PaymentSteps {

    private final BankService bankService;
    private final CustomerService customerService;

    private final MerchantService merchantService;

    private User bankCustomer;
    private User bankMerchant;

    private String customerAccountNumber;
    private String merchantAccountNumber;

    private RegisterCustomerResponse registerCustomerResponse;
    private RegisterCustomerResponse registerMerchantResponse;

    private GenerateTokenResponse generateTokenResponse;

    private boolean wasPaymentSuccessful = false;

    public PaymentSteps(CustomerService customerService, MerchantService merchantService) {
        this.customerService = customerService;
        this.merchantService = merchantService;
        bankService = new BankServiceService().getBankServicePort();
    }


    @Given("a customer with name {string}, last name {string}, and CPR {string}")
    public void aCustomerWithNameLastNameAndCPR(String firstname, String lastName, String cpr) {
        // Write code here that turns the phrase above into concrete actions
        bankCustomer = new User();
        bankCustomer.setCprNumber(cpr);
        bankCustomer.setFirstName(firstname);
        bankCustomer.setLastName(lastName);
    }

    @And("the customer is registered with the bank with an initial balance of {int} kr")
    public void theCustomerIsRegisteredWithTheBankWithAnInitialBalanceOfKr(int amount) throws BankServiceException_Exception {
        customerAccountNumber = bankService.createAccountWithBalance(bankCustomer, BigDecimal.valueOf(amount));
        assertNotNull(customerAccountNumber);
        Users.bankAccounts.add(customerAccountNumber);

    }

    @And("the customer is registered with Simple DTU Pay using their bank account")
    public void theCustomerIsRegisteredWithSimpleDTUPayUsingTheirBankAccount() {
        RegisterCustomerRequest request = new RegisterCustomerRequest(bankCustomer.getFirstName(),
                bankCustomer.getLastName(),
                bankCustomer.getCprNumber(),
                customerAccountNumber);

        registerCustomerResponse = customerService.registerCustomer(request);
        assertNotNull(registerCustomerResponse);
        Users.dtuPayAccounts.add(registerCustomerResponse.id());
    }

    @And("a merchant with name {string}, last name {string}, and CPR {string}")
    public void aMerchantWithNameLastNameAndCPR(String firstname, String lastname, String cpr) {
        bankMerchant = new User();
        bankMerchant.setCprNumber(cpr);
        bankMerchant.setFirstName(firstname);
        bankMerchant.setLastName(lastname);
    }

    @And("the merchant is registered with the bank with an initial balance of {int} kr")
    public void theMerchantIsRegisteredWithTheBankWithAnInitialBalanceOfKr(int amount) throws BankServiceException_Exception {
        merchantAccountNumber = bankService.createAccountWithBalance(bankMerchant, BigDecimal.valueOf(amount));
        assertNotNull(merchantAccountNumber);
        Users.bankAccounts.add(merchantAccountNumber);
    }

    @And("the merchant is registered with Simple DTU Pay using their bank")
    public void theMerchantIsRegisteredWithSimpleDTUPayUsingTheirBank() {
        RegisterCustomerRequest request = new RegisterCustomerRequest(bankMerchant.getFirstName(),
                bankMerchant.getLastName(),
                bankMerchant.getCprNumber(),
                merchantAccountNumber);

        registerMerchantResponse = customerService.registerCustomer(request);
        assertNotNull(registerMerchantResponse);
        assertNotNull(registerMerchantResponse.id());
        Users.dtuPayAccounts.add(registerMerchantResponse.id());
    }

    @And("the customer has generated token")
    public void theCustomerHasGeneratedToken() {
        generateTokenResponse = customerService.generateToken(registerCustomerResponse.id(), 1);

        assertNotNull(generateTokenResponse);

        // one token is generated
        assertEquals(1, generateTokenResponse.tokens().size());
    }

    @When("the merchant initiates a payment for {int} kr by the customer with the valid token")
    public void theMerchantInitiatesAPaymentForKrByTheCustomerWithTheValidToken(int amount) {
        StartPaymentRequest request = new StartPaymentRequest(generateTokenResponse.tokens().getFirst(), amount);

        wasPaymentSuccessful = merchantService.pay(registerMerchantResponse.id(),request);

    }

    @Then("the payment should be successful")
    public void thePaymentShouldBeSuccessful() {
        assertTrue(wasPaymentSuccessful);
    }

    @And("the balance of the customer should be {int} kr")
    public void theBalanceOfTheCustomerShouldBeKr(int expected) throws BankServiceException_Exception {

        int actual = bankService.getAccount(customerAccountNumber).getBalance().intValue();
        assertEquals(expected, actual);
    }

    @And("the balance of the merchant should be {int} kr")
    public void theBalanceOfTheMerchantShouldBeKr(int expected) throws BankServiceException_Exception {
        int actual = bankService.getAccount(merchantAccountNumber).getBalance().intValue();
        assertEquals(expected, actual);
    }
}
