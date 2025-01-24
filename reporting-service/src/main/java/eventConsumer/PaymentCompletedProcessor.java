package eventConsumer;

import core.domain.payment.*;
import core.domainService.PaymentService;
import events.PaymentCompleted;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

/**
 * @author: Satish Gurung (s243872)
 */
@ApplicationScoped
public class PaymentCompletedProcessor {

    private final PaymentService paymentService;

    public PaymentCompletedProcessor(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Incoming("PaymentCompleted")
    public void process(JsonObject request) {
        PaymentCompleted event = request.mapTo(PaymentCompleted.class);
        System.out.println("PaymentCompleted event received: " + event.getCorrelationId());

        if (!event.wasSuccessful()) {
            System.out.println("Ignoring the failed event: " + event.getCorrelationId() + " with error: " + event.getError());
            return;
        }
        Payment payment = Payment.from(
                PaymentId.from(event.getPaymentId()),
                Amount.from(event.getAmount()),
                CustomerId.from(event.getCustomerId()),
                MerchantId.from(event.getMerchantId()),
                Token.from(event.getToken())
        );

        paymentService.addPayment(payment);
    }

}
