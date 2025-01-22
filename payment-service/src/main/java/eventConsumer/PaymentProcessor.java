package eventConsumer;

import core.domain.payment.*;
import core.domainService.PaymentService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import events.AccountValidationCompleted;
import events.PaymentCompleted;
import events.PaymentRequested;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class PaymentProcessor {

    // Corelation id -> Payment
    private final ConcurrentHashMap<String, PaymentCache> correlations = new ConcurrentHashMap<>();

    private final Emitter<PaymentCompleted> paymentCompletedEmitter;

    private final PaymentService paymentService;

    public PaymentProcessor(@Channel("PaymentCompleted") Emitter<PaymentCompleted> paymentCompletedEmitter,
                            PaymentService paymentService) {
        this.paymentCompletedEmitter = paymentCompletedEmitter;
        this.paymentService = paymentService;
    }

    @Incoming("ValidateMerchantCompleted")
    public void processMerchantValidation(JsonObject request) {
        AccountValidationCompleted event = request.mapTo(AccountValidationCompleted.class);
        PaymentCache paymentCache = correlations.get(event.getCorrelationId());

        if (paymentCache == null) {
            correlations.put(event.getCorrelationId(), new PaymentCache());
            // Call recursively
            processMerchantValidation(request);
            return;
        }
        if (!event.wasSuccessful()){
            paymentCache.setError(event.getError());
        }
        else{
            paymentCache.getPayment().setMerchantAccount(BankAccountNumber.from(event.getBankAccountNumber()));
            paymentCache.getPayment().setMerchantId(MerchantId.from(event.getUserId()));
        }
        processAfterAllValues(paymentCache, event.getCorrelationId());
    }

    @Incoming("ValidateCustomerCompleted")
    public void processCustomerValidation(JsonObject request) {
        AccountValidationCompleted event = request.mapTo(AccountValidationCompleted.class);
        PaymentCache paymentCache = correlations.get(event.getCorrelationId());

        if (paymentCache == null) {
            correlations.put(event.getCorrelationId(), new PaymentCache());
            // Call recursively
            processCustomerValidation(request);
            return;
        }
        if (!event.wasSuccessful()){
            paymentCache.setError(event.getError());
        }
        else{
            paymentCache.getPayment().setCustomerAccount(BankAccountNumber.from(event.getBankAccountNumber()));
            paymentCache.getPayment().setCustomerId(CustomerId.from(event.getUserId()));
        }
        processAfterAllValues(paymentCache, event.getCorrelationId());
    }

    @Incoming("PaymentRequested")
    public void processPaymentRequest(JsonObject request) {
        PaymentRequested event = request.mapTo(PaymentRequested.class);

        PaymentCache paymentCache = correlations.get(event.getCorrelationId());

        if (paymentCache == null) {
            correlations.put(event.getCorrelationId(), new PaymentCache());
            // Call recursively
            processPaymentRequest(request);
            return;
        }
        paymentCache.getPayment().setAmount(Amount.from(event.getAmount()));
        processAfterAllValues(paymentCache, event.getCorrelationId());
    }

    private void processAfterAllValues(PaymentCache paymentCache, String correlationId) {
        Payment payment = paymentCache.getPayment();
        if (payment.getAmount() == null || payment.getCustomerAccount() == null || payment.getMerchantAccount() == null) {
            return;
        }

        if (!paymentCache.wasSuccessful()){
            throw new RuntimeException(paymentCache.getError());
        }

        try {
            paymentService.processPayment(payment);

            PaymentCompleted completedEvent = new PaymentCompleted(
                    correlationId,
                    null,
                    payment.getId().getValue(),
                    payment.getToken().getValue(),
                    payment.getAmount().getValue(),
                    payment.getMerchantId().getValue(),
                    payment.getCustomerId().getValue()
            );

            paymentCompletedEmitter.send(completedEvent);
        } catch (BankServiceException_Exception e) {
            PaymentCompleted completedEvent = new PaymentCompleted(
                    correlationId,
                    null,
                    e.getMessage()
            );
            paymentCompletedEmitter.send(completedEvent);
        }
    }
}
