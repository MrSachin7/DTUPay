package core.domain.payment;

import core.domain.common.Id;

import java.util.UUID;

public class MerchantId extends Id {

    private MerchantId() {
    }

    public static MerchantId from(String id){
        MerchantId merchantId = new MerchantId();
        merchantId.setId(UUID.fromString(id));
        return merchantId;
    }

}
