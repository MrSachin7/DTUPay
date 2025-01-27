package core.domainService;

import core.domain.payment.Payment;
import core.domain.payment.PaymentRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author: Tomas Durnek (s233799)
 */
@ApplicationScoped
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public void addPayment(Payment payment) {
        System.out.println("Adding payment to repository   "+  payment.getId().getValue());
        repository.add(payment);
    }

}
