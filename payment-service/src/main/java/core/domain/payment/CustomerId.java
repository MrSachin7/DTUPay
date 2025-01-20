package core.domain.payment;

import core.domain.common.Id;

import java.util.UUID;

public class CustomerId extends Id {
    private CustomerId() {
    }

    public static CustomerId from(String id) {
        CustomerId merchantId = new CustomerId();
        merchantId.value = UUID.fromString(id);
        return merchantId;
    }

}
