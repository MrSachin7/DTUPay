package core.domainService;

import core.domain.payment.Payment;
import core.domain.payment.PaymentRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public void addPayment(Payment payment) {
        repository.add(payment);
    }

}
