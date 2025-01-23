package cucumbertests;

import core.domain.payment.*;
import core.domainService.ReportService;
import eventConsumer.ReportsRequestedProcessor;
import events.ReportType;
import events.ReportsRequested;
import events.ReportsRetrieved;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RetrieveReportsSteps {

    private final PaymentRepository repository;
    private final ReportsRequestedProcessor processor;
    private ReportsRequested event;
    private ReportsRetrieved result;

    CustomerId customerId = CustomerId.from(UUID.randomUUID().toString());
    MerchantId merchantId = MerchantId.from(UUID.randomUUID().toString());

    Payment payment1 = Payment.from(
            PaymentId.from(UUID.randomUUID().toString()),
            Amount.from(100.0),
            customerId,
            MerchantId.from(UUID.randomUUID().toString()),
            Token.from(UUID.randomUUID().toString())
    );

    Payment payment2 = Payment.from(
            PaymentId.from(UUID.randomUUID().toString()),
            Amount.from(15.5),
            customerId,
            merchantId,
            Token.from(UUID.randomUUID().toString())
    );

    Payment payment3 = Payment.from(
            PaymentId.from(UUID.randomUUID().toString()),
            Amount.from(184.0),
            CustomerId.from(UUID.randomUUID().toString()),
            merchantId,
            Token.from(UUID.randomUUID().toString())
    );

    public RetrieveReportsSteps() {
        this.repository = mock(PaymentRepository.class);
        ReportService reportService = new ReportService(repository);
        processor = new ReportsRequestedProcessor(reportService);

        when(repository.getAllPayments()).thenReturn(List.of(payment1, payment2, payment3));
        when(repository.getPaymentsByCustomerId(customerId)).thenReturn(List.of(payment1, payment2));
        when(repository.getPaymentsByMerchantId(merchantId)).thenReturn(List.of(payment2, payment3));
    }

    @When("the system receives a ReportsRequested event for all reports")
    public void receiveAllReportsRequestedEvent() {
        event = new ReportsRequested("correlationId", ReportType.ALL_REPORTS, null);
        result = processor.process(JsonObject.mapFrom(event));
    }

    @Then("the system retrieves all payments from the repository")
    public void verifyAllPaymentsRetrieved() {
        List<Payment> payments = repository.getAllPayments();
        Assertions.assertEquals(payments.size(), result.getPaymentData().size());
    }

    @Then("the system publishes a ReportsRetrieved event with all payment data")
    public void verifyAllReportsRetrievedEvent() {
        Assertions.assertNotNull(result.getPaymentData());
        Assertions.assertTrue(result.wasSuccessful());
        Assertions.assertEquals(repository.getAllPayments().size(), result.getPaymentData().size());
    }

    @When("the system receives a ReportsRequested event for customer reports with a valid customer ID")
    public void receiveCustomerReportsRequestedEvent() {
        event = new ReportsRequested("correlationId", ReportType.CUSTOMER_REPORTS, customerId.getValue());
        result = processor.process(JsonObject.mapFrom(event));
    }

    @Then("the system retrieves payments for that customer from the repository")
    public void verifyCustomerPaymentsRetrieved() {
        List<Payment> payments = repository.getPaymentsByCustomerId(customerId);
        Assertions.assertEquals(payments.size(), result.getPaymentData().size());
    }

    @Then("the system publishes a ReportsRetrieved event with the customer's payment data")
    public void verifyCustomerReportsRetrievedEvent() {
        Assertions.assertNotNull(result.getPaymentData());
        Assertions.assertTrue(result.wasSuccessful());
        Assertions.assertEquals(repository.getPaymentsByCustomerId(customerId).size(), result.getPaymentData().size());
    }

    @When("the system receives a ReportsRequested event for merchant reports with a valid merchant ID")
    public void receiveMerchantReportsRequestedEvent() {
        event = new ReportsRequested("correlationId", ReportType.MERCHANT_REPORTS, merchantId.getValue());
        result = processor.process(JsonObject.mapFrom(event));
    }

    @Then("the system retrieves payments for that merchant from the repository")
    public void verifyMerchantPaymentsRetrieved() {
        List<Payment> payments = repository.getPaymentsByMerchantId(merchantId);
        Assertions.assertEquals(payments.size(), result.getPaymentData().size());
    }

    @Then("the system publishes a ReportsRetrieved event with the merchant's payment data")
    public void verifyMerchantReportsRetrievedEvent() {
        Assertions.assertNotNull(result.getPaymentData());
        Assertions.assertTrue(result.wasSuccessful());
        Assertions.assertEquals(repository.getPaymentsByMerchantId(merchantId).size(), result.getPaymentData().size());
    }

    @Then("the customer id is omitted")
    public void verifyCustomerIdIsOmitted() {
        Assertions.assertNotNull(result.getPaymentData());
        boolean isCustomerIdOmitted = result.getPaymentData().stream()
                .allMatch(paymentData -> paymentData.getCustomerId() == null);
        Assertions.assertTrue(isCustomerIdOmitted, "Customer ID should be omitted in all payment data");
    }

    @When("the system receives a ReportsRequested event with an invalid report type")
    public void receiveInvalidReportTypeEvent() {
        event = new ReportsRequested("correlationId", null, null);
        result = processor.process(JsonObject.mapFrom(event));
    }

    @When("the system receives a ReportsRequested event for customer reports with a missing customer ID")
    public void receiveMissingCustomerIdEvent() {
        event = new ReportsRequested("correlationId", ReportType.CUSTOMER_REPORTS, null);
        result = processor.process(JsonObject.mapFrom(event));
    }

    @When("the system receives a ReportsRequested event for merchant reports with a missing merchant ID")
    public void receiveMissingMerchantIdEvent() {
        event = new ReportsRequested("correlationId", ReportType.MERCHANT_REPORTS, null);
        result = processor.process(JsonObject.mapFrom(event));
    }

    @Then("the system publishes a ReportsRetrieved event with an error message {string}")
    public void verifyErrorMessage(String errorMessage) {
        Assertions.assertNotNull(result.getError());
        Assertions.assertEquals(errorMessage, result.getError());
    }
}