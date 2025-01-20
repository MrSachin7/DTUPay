package core.domain.payment;

import core.domain.common.Id;

import java.util.UUID;

public class PaymentId extends Id {
    private PaymentId() {
    }

    public static PaymentId newPaymentId() {
        PaymentId id = new PaymentId();
        id.value = UUID.randomUUID();
        return id;
    }
}
