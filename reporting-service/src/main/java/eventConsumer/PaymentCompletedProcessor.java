package eventConsumer;

import core.domainService.PaymentService;
import events.PaymentCompleted;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PaymentCompletedProcessor {

    private final PaymentService paymentService;

    private PaymentCompletedProcessor(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Incoming("PaymentCompleted")
    public void process(JsonObject request) {
        PaymentCompleted event = request.mapTo(PaymentCompleted.class);

        if (!event.wasSuccessful())
            return;

        paymentService.addPayment();
    }

}
