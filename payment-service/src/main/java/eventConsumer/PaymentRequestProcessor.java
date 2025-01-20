package eventConsumer;

import core.domain.PaymentRequest;
import core.domain.Payment;
import core.domainService.PaymentProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PaymentRequestProcessor {

    private final PaymentProcessor paymentProcessor;

    public PaymentRequestProcessor(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    @Incoming("payment-requests")
    public void handlePaymentRequest(PaymentRequest request) {
        Payment payment = new Payment(request.getPaymentId(), request.getCustomerId(), request.getAmount());
        paymentProcessor.processPayment(payment);
        System.out.println("Processed payment for customer: " + request.getCustomerId());
    }
}
