package eventConsumer;

import events.PaymentProcessed;
import events.PaymentRequested;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
public class PaymentRequestProcessor {
    @Incoming("PaymentRequested")
    @Outgoing("PaymentProcessed")
    public PaymentProcessed process(PaymentRequested request) {
        System.out.println("Received PaymentRequested message: " + request);
        System.out.println("Processing payment: " + request.getTokenId());
        return new PaymentProcessed(request.getTokenId(), request.getMerchantId(), request.getAmount());
    }
}
