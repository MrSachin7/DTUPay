package core.domain.token;

import core.domain.common.Id;

import java.util.UUID;

public class CustomerId extends Id {

    private CustomerId() {
    }

    public static CustomerId newCustomerId() {
        CustomerId customerId = new CustomerId();
        customerId.setId(UUID.randomUUID());
        return customerId;
    }

    public static CustomerId from(String id){
        CustomerId customerId = new CustomerId();
        customerId.setId(UUID.fromString(id));
        return customerId;
    }

}
