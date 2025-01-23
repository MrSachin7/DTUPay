package features;

import dto.*;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import services.CustomerService;
import services.MerchantService;
import services.ReportService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReportSteps {

    private final CustomerService customerService;
    private final MerchantService merchantService;
    private final ReportService reportService;

    private RegisterCustomerResponse registerCustomerResponse1, registerMerchantResponse, registerCustomerResponse2;
    private GenerateReportsResponse generateReportsResponse;
    private GenerateTokenResponse generateTokenResponse1;

    private Exception retrieveReportsException;

    private BankService bankService;

    private final List<String> paymentsMade;

    public ReportSteps() {
        merchantService = new MerchantService();
        customerService = new CustomerService();
        bankService = new BankServiceService().getBankServicePort();

        reportService = new ReportService();
        paymentsMade = new ArrayList<>(5);
    }

    @Given("that a multiple payments are made successfully in the system")
    public void thatAMultiplePaymentsAreMadeSuccessfullyInTheSystem() throws Exception {
        // Customer registration
        User customer1 = new User();
        customer1.setCprNumber("1230257842");
        customer1.setFirstName("Tomas");
        customer1.setLastName("Durnek");
        String customerBankAccount1 = bankService.createAccountWithBalance(customer1, BigDecimal.valueOf(10000));
        Users.bankAccounts.add(customerBankAccount1);

        registerCustomerResponse1 = customerService.registerCustomer(new RegisterCustomerRequest(customer1.getFirstName(), customer1.getLastName(), customer1.getCprNumber(), customerBankAccount1));
        Users.dtuPayAccounts.add(registerCustomerResponse1.id());

        // Second customer registration
        User customer2 = new User();
        customer2.setCprNumber("1820257842");
        customer2.setFirstName("Satish");
        customer2.setLastName("Gurung");
        String customerBankAccount2 = bankService.createAccountWithBalance(customer2, BigDecimal.valueOf(10000));
        Users.bankAccounts.add(customerBankAccount2);
        registerCustomerResponse2 = customerService.registerCustomer(new RegisterCustomerRequest(customer2.getFirstName(), customer2.getLastName(), customer2.getCprNumber(), customerBankAccount2));
        Users.dtuPayAccounts.add(registerCustomerResponse1.id());

        // Merchant registration
        User merchant = new User();
        merchant.setCprNumber("4258931476");
        merchant.setFirstName("Sachin");
        merchant.setLastName("Baral");
        String merchantBankAccount = bankService.createAccountWithBalance(merchant, BigDecimal.valueOf(10000));
        Users.bankAccounts.add(merchantBankAccount);
        registerMerchantResponse = merchantService.registerMerchant(new RegisterCustomerRequest(merchant.getFirstName(), merchant.getLastName(), merchant.getCprNumber(), merchantBankAccount));
        Users.dtuPayAccounts.add(registerMerchantResponse.id());

        assertNotNull(registerCustomerResponse1);
        assertNotNull(registerMerchantResponse);
        assertNotNull(registerCustomerResponse2);

        generateTokenResponse1 = customerService.generateToken(registerCustomerResponse1.id(), 5);
        GenerateTokenResponse generateTokenResponse2 = customerService.generateToken(registerCustomerResponse2.id(), 5);

        assertNotNull(generateTokenResponse1);
        // First payment
        StartPaymentRequest request = new StartPaymentRequest(generateTokenResponse2.tokens().getFirst(), 100);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request));

        // Second payment
        request = new StartPaymentRequest(generateTokenResponse2.tokens().get(1), 200);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request));

        // Third payment
        request = new StartPaymentRequest(generateTokenResponse2.tokens().getFirst(), 300);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request));

        // Fourth payment
        request = new StartPaymentRequest(generateTokenResponse2.tokens().get(1), 400);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request));
    }

    @When("the reporting service is called to generate a report")
    public void theReportingServiceIsCalledToGenerateAReport() throws Exception {
        generateReportsResponse = reportService.generateReports();
    }

    @Then("the report should be generated successfully")
    public void theReportShouldBeGeneratedSuccessfully() {
        assertNotNull(generateReportsResponse);
    }

    @And("the payments must be included in the report")
    public void thePaymentsMustBeIncludedInTheReport() {
        List<ReportData> payments = generateReportsResponse.payments();

        for (String id : paymentsMade) {
            boolean match = payments.stream().anyMatch(p -> p.paymentId().equals(id));
            assertTrue(match);
        }
    }

    @Given("that multiple payments are made successfully in the system by a specific customer")
    public void thatAMultiplePaymentsAreMadeSuccessfullyInTheSystemByASpecificCustomer() {
        // First payment
        StartPaymentRequest request = new StartPaymentRequest(generateTokenResponse1.tokens().getFirst(), 15.5);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request));

        // Second payment
        request = new StartPaymentRequest(generateTokenResponse1.tokens().get(1), 122.33);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request));
    }

    @When("the reporting service is called to generate a report for the customer")
    public void theReportingServiceIsCalledToGenerateAReportForTheCustomer() {
        try {
            generateReportsResponse = customerService.retrieveCustomerReports(registerCustomerResponse1.id());
            assertNotNull(generateReportsResponse);
        } catch (Exception e) {
            retrieveReportsException = e;
        }

        assertNotNull(retrieveReportsException);
    }

    @And("there should not be any reports for other customers")
    public void thereShouldNotBeAnyReportsForOtherCustomers() {
        for (ReportData payment : generateReportsResponse.payments()) {
            assertEquals(payment.customerId(), registerCustomerResponse1.id());
        }
    }

    @Given("that multiple payments are made successfully in the system to a specific merchant")
    public void thatAMultiplePaymentsAreMadeSuccessfullyInTheSystemToASpecificMerchant() {
    }

    @When("the reporting service is called to generate a report for the merchant")
    public void theReportingServiceIsCalledToGenerateAReportForTheMerchant() {
    }

    @And("there should not be any reports for other merchants")
    public void thereShouldNotBeAnyReportsForOtherMerchants() {
    }

    @And("the reports must not contain any information about the customers")
    public void theReportsMustNotContainAnyInformationAboutTheCustomers() {
    }
}
