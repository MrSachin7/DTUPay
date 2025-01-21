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

    private ReportsRequestedProcessor(ReportService reportService) {
        this.reportService = reportService;
    }

    @Incoming("ReportsRequested")
    @Outgoing("ReportsRetrieved")
    public ReportsRetrieved process(JsonObject request) {
        ReportsRequested event = request.mapTo(ReportsRequested.class);

        List<Payment> payments = switch (event.getReportType()) {
            case ALL_REPORTS -> reportService.getReportsForAllPayments();
            case CUSTOMER_REPORTS -> reportService.getReportsForCustomer(event.getId());
            case MERCHANT_REPORTS -> reportService.getReportsForMerchant(event.getId());
            default -> new ArrayList<>();
        };

        List<ReportsRetrieved.PaymentData> paymentDatas = payments.stream().map(payment -> {
            ReportsRetrieved.PaymentData paymentData = new ReportsRetrieved.PaymentData();

            if (payment.getCustomerId() != null)
            {
                paymentData.setCustomerId(payment.getCustomerId().getValue());
            }

            paymentData.setMerchantId(payment.getMerchantId().getValue());
            paymentData.setToken(payment.getToken().getValue());
            paymentData.setAmount(payment.getAmount().getValue());

            return paymentData;
        }).toList();

        return new ReportsRetrieved(event.getCorrelationId(), paymentDatas)
    }

}
