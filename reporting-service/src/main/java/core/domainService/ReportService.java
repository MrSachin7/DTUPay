package core.domainService;

import core.domain.payment.CustomerId;
import core.domain.payment.MerchantId;
import core.domain.payment.Payment;
import core.domain.payment.PaymentRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ReportService {

    private final PaymentRepository repository;

    public ReportService(PaymentRepository Repository) {
        this.repository = Repository;
    }

    public List<Payment> getReportsForAllPayments() {
        return repository.getAllPayments();
    }

    public List<Payment> getReportsForCustomer(String id) {
        return repository.getPaymentsByCustomerId(CustomerId.from(id));
    }

    public List<Payment> getReportsForMerchant(String id) {
        List<Payment> payments = repository.getPaymentsByMerchantId(MerchantId.from(id));
        return payments.stream().map(payment -> Payment.from(payment.getId(),
                payment.getAmount(),
                null,
                payment.getMerchantId(),
                payment.getToken())).toList();
    }
}
