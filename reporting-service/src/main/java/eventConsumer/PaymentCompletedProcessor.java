package eventConsumer;

import core.domain.payment.*;
import core.domainService.PaymentService;
import events.PaymentCompleted;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PaymentCompletedProcessor {

    private final PaymentService paymentService;

    public PaymentCompletedProcessor(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Incoming("PaymentCompleted")
    public void process(JsonObject request) {
        PaymentCompleted event = request.mapTo(PaymentCompleted.class);

        if (!event.wasSuccessful())
            return;

        System.out.println("PaymentCompleted: " + event.getCorrelationId());

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
