package core.domainService;
import core.domain.Payment;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentProcessor {
    public void processPayment(Payment payment) {
        if (payment.getAmount() <= 0) {
            payment.fail();
            throw new IllegalArgumentException("Invalid payment amount");
        }

        payment.complete();
    }
}
