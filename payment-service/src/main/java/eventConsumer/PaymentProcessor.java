package eventConsumer;

import core.domainService.PaymentService;
import events.*;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.*;

import java.util.concurrent.*;

/**
 * @author: Ari Sigþór Eiríksson (s232409)
 */
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
        String correlationId = event.getCorrelationId();
        System.out.println("ValidateMerchantCompleted event received: " + correlationId);

        paymentContexts.compute(correlationId, (id, context) -> {
            if (context == null) {
                context = new PaymentContext();
            }

            if (!event.wasSuccessful()) {
                // Immediately emit failure if merchant validation fails
                System.out.println("Merchant validation failed: " + event.getError());
                handlePaymentError(correlationId, new RuntimeException(event.getError()));
                return context;
            }

            context.merchantAccount = event.getBankAccountNumber();
            tryProcessPayment(id, context);
            return context;
        });
    }

    @Incoming("ValidateCustomerCompleted")
    public void processCustomerValidation(JsonObject request) {
        AccountValidationCompleted event = request.mapTo(AccountValidationCompleted.class);
        String correlationId = event.getCorrelationId();
        System.out.println("ValidateCustomerCompleted event received: " + correlationId);

        paymentContexts.compute(correlationId, (id, context) -> {
            if (context == null) {
                context = new PaymentContext();
            }

            if (!event.wasSuccessful()) {
                // Immediately emit failure if customer validation fails
                handlePaymentError(correlationId, new RuntimeException(event.getError()));
                return context;
            }

            context.customerAccount = event.getBankAccountNumber();
            context.customerId = event.getUserId();
            tryProcessPayment(correlationId, context);
            return context;
        });
    }

    @Incoming("PaymentRequested")
    public void processPaymentRequest(JsonObject request) {
        PaymentRequested event = request.mapTo(PaymentRequested.class);
        String correlationId = event.getCorrelationId();
        System.out.println("PaymentRequested event received: " + correlationId);

        paymentContexts.compute(correlationId, (id, context) -> {
            if (context == null) {
                context = new PaymentContext();
            }
            context.amount = event.getAmount();
            context.startTime = System.currentTimeMillis();
            context.merchantId = event.getMerchantId();
            context.token = event.getToken();

            scheduleTimeout(id);
            tryProcessPayment(id, context);
            return context;
        });
    }

    private void scheduleTimeout(String correlationId) {
        CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS).execute(() -> {
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
                PaymentContext removed = paymentContexts.remove(correlationId);
                System.out.println("PaymentCompleted event sent with success: " + correlationId);
                emitPaymentCompleted(correlationId, paymentId, null, removed.token, removed.amount, removed.merchantId, removed.customerId);
            } catch (Exception e) {
                handlePaymentError(correlationId, e);
            }
        }
    }

    private void handlePaymentError(String correlationId, Throwable error) {
        PaymentContext context = paymentContexts.remove(correlationId);
        if (context != null && !context.isComplete) {
            context.isComplete = true;
            System.out.println("PaymentCompleted event sent with error: " + correlationId);

            emitPaymentCompleted(correlationId, null, error.getMessage(), context.token, context.amount, context.merchantId, context.customerId);
        }
    }

    private void emitPaymentCompleted(String correlationId, String paymentId, String error, String token, double amount, String merchantId, String customerId) {
        PaymentCompleted completedEvent = new PaymentCompleted(correlationId, error, paymentId, token, amount, merchantId, customerId);
        paymentCompletedEmitter.send(completedEvent);
    }

    private static class PaymentContext {
        String customerAccount;
        String merchantAccount;
        double amount;
        long startTime;
        boolean isComplete;
        String token;
        String merchantId;
        String customerId;

        boolean isReadyForProcessing() {
            return customerAccount != null &&
                    merchantAccount != null &&
                    amount > 0;
        }
    }
}