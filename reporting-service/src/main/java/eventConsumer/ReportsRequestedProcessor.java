package eventConsumer;

import core.domain.payment.Payment;
import core.domainService.ReportService;
import events.ReportsRequested;
import events.ReportsRetrieved;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ReportsRequestedProcessor {

    private final ReportService reportService;

    public ReportsRequestedProcessor(ReportService reportService) {
        this.reportService = reportService;
    }

    @Incoming("ReportsRequested")
    @Outgoing("ReportsRetrieved")
    public ReportsRetrieved process(JsonObject request) {
        ReportsRequested event = request.mapTo(ReportsRequested.class);

        try {
            if (event.getReportType() == null)
                throw new IllegalArgumentException("Invalid report type requested");

            List<Payment> payments = getPayments(event);

            List<ReportsRetrieved.PaymentData> paymentData = payments.stream().map(payment -> {
                ReportsRetrieved.PaymentData data = new ReportsRetrieved.PaymentData();

                if (payment.getCustomerId() != null)
                    data.setCustomerId(payment.getCustomerId().getValue());

                data.setMerchantId(payment.getMerchantId().getValue());
                data.setToken(payment.getToken().getValue());
                data.setAmount(payment.getAmount().getValue());

                return data;
            }).toList();

            return new ReportsRetrieved(event.getCorrelationId(), paymentData);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            System.err.println("Error processing ReportsRequested event: " + errorMessage);
            ReportsRetrieved responseEvent = new ReportsRetrieved(event.getCorrelationId(), null);
            responseEvent.setError(errorMessage);

            return responseEvent;
        }
    }

    private List<Payment> getPayments(ReportsRequested event) {
        return switch (event.getReportType())
                {
                    case ALL_REPORTS -> reportService.getReportsForAllPayments();
                    case CUSTOMER_REPORTS -> {
                        if (event.getId() == null)
                            throw new IllegalArgumentException("Customer ID is required");

                        yield reportService.getReportsForCustomer(event.getId());
                    }
                    case MERCHANT_REPORTS -> {
                        if (event.getId() == null)
                            throw new IllegalArgumentException("Merchant ID is required");

                        yield reportService.getReportsForMerchant(event.getId());
                    }
                    default -> throw new IllegalArgumentException("Unsupported report type");
                };
    }

}
