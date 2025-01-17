package core.domain.account;

import core.domain.common.ValueObject;

public class CprNumber extends ValueObject {
    private String value;

    private CprNumber(){
    }

    public static CprNumber from(String value){
        if (value.length() != 10) {
            throw new IllegalArgumentException("CPR number must be 10 characters long");
        }
        CprNumber cprNumber = new CprNumber();
        cprNumber.value = value;
        return cprNumber;
    }

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{value};
    }
}
