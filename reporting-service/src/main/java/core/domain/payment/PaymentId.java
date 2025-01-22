package core.domain.payment;

import core.domain.common.Id;

import java.util.UUID;

public class PaymentId extends Id {

    private PaymentId() {
    }

    public static PaymentId from(String id){
        PaymentId paymentId = new PaymentId();
        paymentId.setId(UUID.fromString(id));
        return paymentId;
    }

}
