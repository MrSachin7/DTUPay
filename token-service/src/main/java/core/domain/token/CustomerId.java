package core.domain.token;

import core.domain.common.Id;

import java.util.UUID;

public class CustomerId extends Id {

    private CustomerId(String id) {
    }

    public static CustomerId newCustomerId(String id) {
        CustomerId customerId = new CustomerId(id);
        customerId.value = UUID.randomUUID();
        return customerId;
    }

}
