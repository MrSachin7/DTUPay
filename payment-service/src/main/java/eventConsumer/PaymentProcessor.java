package eventConsumer;

import core.domain.payment.*;
import core.domainService.PaymentService;
import events.*;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.*;

import java.util.concurrent.*;

@ApplicationScoped
public class PaymentProcessor {
    private final ConcurrentHashMap<String, PaymentContext> paymentContexts = new ConcurrentHashMap<>();
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
        System.out.println("MerchantValidationCompleted: " + event.getCorrelationId());

        paymentContexts.compute(event.getCorrelationId(), (id, context) -> {
            if (context == null) {
                context = new PaymentContext();
            }
            context.merchantAccount = event.getBankAccountNumber();
            tryProcessPayment(id, context);
            return context;
        });
    }

    @Incoming("ValidateCustomerCompleted")
    public void processCustomerValidation(JsonObject request) {
        AccountValidationCompleted event = request.mapTo(AccountValidationCompleted.class);
        System.out.println("CustomerValidationCompleted: " + event.getCorrelationId());

        paymentContexts.compute(event.getCorrelationId(), (id, context) -> {
            if (context == null) {
                context = new PaymentContext();
            }
            context.customerAccount = event.getBankAccountNumber();
            tryProcessPayment(id, context);
            return context;
        });
    }

    @Incoming("PaymentRequested")
    public void processPaymentRequest(JsonObject request) {
        PaymentRequested event = request.mapTo(PaymentRequested.class);
        System.out.println("PaymentRequested: " + event.getCorrelationId());

        paymentContexts.compute(event.getCorrelationId(), (id, context) -> {
            if (context == null) {
                context = new PaymentContext();
            }
            context.amount = event.getAmount();
            context.startTime = System.currentTimeMillis();

            // Schedule timeout check
            scheduleTimeout(id);

            tryProcessPayment(id, context);
            return context;
        });
    }

    private void scheduleTimeout(String correlationId) {
        CompletableFuture.delayedExecutor(30, TimeUnit.SECONDS).execute(() -> {
            PaymentContext context = paymentContexts.get(correlationId);
            if (context != null && !context.isComplete) {
                handlePaymentError(correlationId, new TimeoutException("Payment processing timed out"));
            }
        });
    }

    private synchronized void tryProcessPayment(String correlationId, PaymentContext context) {
        if (context.isComplete) {
            return;
        }

        if (context.isReadyForProcessing()) {
            try {
                String paymentId = paymentService.processPayment(
                        context.customerAccount,
                        context.merchantAccount,
                        context.amount
                );

                context.isComplete = true;
                paymentContexts.remove(correlationId);
                emitPaymentCompleted(correlationId, paymentId, null);
            } catch (Exception e) {
                handlePaymentError(correlationId, e);
            }
        }
    }

    private void handlePaymentError(String correlationId, Throwable error) {
        PaymentContext context = paymentContexts.remove(correlationId);
        if (context != null && !context.isComplete) {
            context.isComplete = true;
            emitPaymentCompleted(correlationId, null, error.getMessage());
        }
    }

    private void emitPaymentCompleted(String correlationId, String paymentId, String error) {
        PaymentCompleted completedEvent = new PaymentCompleted(correlationId, paymentId, error);
        paymentCompletedEmitter.send(completedEvent);
    }

    private static class PaymentContext {
        String customerAccount;
        String merchantAccount;
        double amount;
        long startTime;
        boolean isComplete;

        boolean isReadyForProcessing() {
            return customerAccount != null &&
                    merchantAccount != null &&
                    amount > 0;
        }
    }
}