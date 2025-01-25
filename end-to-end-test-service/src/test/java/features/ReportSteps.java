package features;

import dto.*;
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
    private GenerateTokenResponse firstCustomerTokens;

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
        customer1.setCprNumber("1589578240");
        customer1.setFirstName("Tomdgfassz");
        customer1.setLastName("Durnegfksx");
        String customerBankAccount1 = bankService.createAccountWithBalance(customer1, BigDecimal.valueOf(10000));
        Users.bankAccounts.add(customerBankAccount1);

        registerCustomerResponse1 = customerService.registerCustomer(new RegisterCustomerRequest(customer1.getFirstName(), customer1.getLastName(), customer1.getCprNumber(), customerBankAccount1));
        Users.dtuPayAccounts.add(registerCustomerResponse1.id());

        // Second customer registration
        User customer2 = new User();
        customer2.setCprNumber("1375245630");
        customer2.setFirstName("Satifsdsshs");
        customer2.setLastName("Gurunsdfsgs");
        String customerBankAccount2 = bankService.createAccountWithBalance(customer2, BigDecimal.valueOf(10000));
        Users.bankAccounts.add(customerBankAccount2);
        registerCustomerResponse2 = customerService.registerCustomer(new RegisterCustomerRequest(customer2.getFirstName(), customer2.getLastName(), customer2.getCprNumber(), customerBankAccount2));
        Users.dtuPayAccounts.add(registerCustomerResponse1.id());

        // Merchant registration
        User merchant = new User();
        merchant.setCprNumber("1248963808");
        merchant.setFirstName("Saxcvchians");
        merchant.setLastName("Barafdslss");
        String merchantBankAccount = bankService.createAccountWithBalance(merchant, BigDecimal.valueOf(10000));
        Users.bankAccounts.add(merchantBankAccount);
        registerMerchantResponse = merchantService.registerMerchant(new RegisterCustomerRequest(merchant.getFirstName(), merchant.getLastName(), merchant.getCprNumber(), merchantBankAccount));
        Users.dtuPayAccounts.add(registerMerchantResponse.id());

        assertNotNull(registerCustomerResponse1);
        assertNotNull(registerMerchantResponse);
        assertNotNull(registerCustomerResponse2);

        firstCustomerTokens = customerService.generateToken(registerCustomerResponse1.id(), 5);
        GenerateTokenResponse secondCustomerTokens = customerService.generateToken(registerCustomerResponse2.id(), 5);


        assertNotNull(firstCustomerTokens);
        // First payment

        StartPaymentRequest request1 = new StartPaymentRequest(firstCustomerTokens.tokens().removeFirst(), 100);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request1));

        // Second payment
        StartPaymentRequest request2 = new StartPaymentRequest(firstCustomerTokens.tokens().removeFirst(), 200);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request2));

        // Third payment
        StartPaymentRequest request3 = new StartPaymentRequest(secondCustomerTokens.tokens().removeFirst(), 300);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request3));

        // Fourth payment
        StartPaymentRequest request4 = new StartPaymentRequest(secondCustomerTokens.tokens().removeFirst(), 400);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request4));
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
    public void thatAMultiplePaymentsAreMadeSuccessfullyInTheSystemByASpecificCustomer() throws Exception {
        // Customer registration
        User customer1 = new User();
        customer1.setCprNumber("1558652010");
        customer1.setFirstName("Tomsdfass");
        customer1.setLastName("Durnsdfeks");
        String customerBankAccount1 = bankService.createAccountWithBalance(customer1, BigDecimal.valueOf(10000));
        Users.bankAccounts.add(customerBankAccount1);

        registerCustomerResponse1 = customerService.registerCustomer(new RegisterCustomerRequest(customer1.getFirstName(), customer1.getLastName(), customer1.getCprNumber(), customerBankAccount1));
        Users.dtuPayAccounts.add(registerCustomerResponse1.id());


        // Merchant registration
        User merchant = new User();
        merchant.setCprNumber("1204586320");
        merchant.setFirstName("Sachfdgins");
        merchant.setLastName("Baragdfls");
        String merchantBankAccount = bankService.createAccountWithBalance(merchant, BigDecimal.valueOf(10000));
        Users.bankAccounts.add(merchantBankAccount);
        registerMerchantResponse = merchantService.registerMerchant(new RegisterCustomerRequest(merchant.getFirstName(), merchant.getLastName(), merchant.getCprNumber(), merchantBankAccount));
        Users.dtuPayAccounts.add(registerMerchantResponse.id());

        assertNotNull(registerCustomerResponse1);
        assertNotNull(registerMerchantResponse);

        firstCustomerTokens = customerService.generateToken(registerCustomerResponse1.id(), 5);

        assertNotNull(firstCustomerTokens);
        // First payment

        StartPaymentRequest request1 = new StartPaymentRequest(firstCustomerTokens.tokens().removeFirst(), 100);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request1));

        // Second payment
        StartPaymentRequest request2 = new StartPaymentRequest(firstCustomerTokens.tokens().removeFirst(), 200);
        paymentsMade.add(merchantService.pay(registerMerchantResponse.id(), request2));

    }

    @When("the reporting service is called to generate a report for the customer")
    public void theReportingServiceIsCalledToGenerateAReportForTheCustomer() {
        try {
            generateReportsResponse = customerService.retrieveCustomerReports(registerCustomerResponse1.id());
            assertNotNull(generateReportsResponse);
        } catch (Exception e) {
            retrieveReportsException = e;
        }

        assertNull(retrieveReportsException);
    }

    @And("there should not be any reports for other customers")
    public void thereShouldNotBeAnyReportsForOtherCustomers() {
        for (ReportData payment : generateReportsResponse.payments()) {
            assertEquals(payment.customerId(), registerCustomerResponse1.id());
        }
    }

    @When("the reporting service is called to generate a report for the merchant")
    public void theReportingServiceIsCalledToGenerateAReportForTheMerchant() {
        try {
            generateReportsResponse = merchantService.retrieveMerchantReports(registerMerchantResponse.id());
            assertNotNull(generateReportsResponse);
        } catch (Exception e) {
            retrieveReportsException = e;
        }
        assertNull(retrieveReportsException);
    }

    @And("there should not be any reports for other merchants")
    public void thereShouldNotBeAnyReportsForOtherMerchants() {
        for (ReportData payment : generateReportsResponse.payments()) {
            assertEquals(payment.merchantId(), registerMerchantResponse.id());
        }
    }

    @And("the reports must not contain any information about the customers")
    public void theReportsMustNotContainAnyInformationAboutTheCustomers() {
        for (ReportData payment : generateReportsResponse.payments()) {
            assertNull(payment.customerId());
        }
    }
}
