package persistence;

import core.domain.payment.*;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class PaymentRepositoryImpl implements PaymentRepository {
    private final Map<PaymentId, Payment> payments = new HashMap<>();

    @Override
    public Payment find(PaymentId paymentId) {
        return payments.get(paymentId);
    }

    @Override
    public void add(Payment aggregate) {
        payments.put(aggregate.getId(), aggregate);
    }

    @Override
    public void remove(Payment aggregate) {
        payments.remove(aggregate.getId());
    }

    @Override
    public List<Payment> getAllPayments() {
        return payments.values().stream().toList();
    }

    @Override
    public List<Payment> getPaymentsByCustomerId(CustomerId id) {
        return payments.values().stream()
                .filter(payment -> payment.getCustomerId().equals(id))
                .toList();
    }

    @Override
    public List<Payment> getPaymentsByMerchantId(MerchantId id) {
        return payments.values().stream()
                .filter(payment -> payment.getMerchantId().equals(id))
                .toList();
    }
}
