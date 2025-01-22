package core.domain.payment;

import core.domain.common.Repository;

import java.util.List;

public interface PaymentRepository extends Repository<Payment, PaymentId> {
    List<Payment> getAllPayments();

    List<Payment> getPaymentsByCustomerId(CustomerId customerId);

    List<Payment> getPaymentsByMerchantId(MerchantId merchantId);
}
