package core.domain.payment;

import core.domain.common.ValueObject;

public class Token extends ValueObject {
    private String value;

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{value};
    }

    public String getValue() {
        return value;
    }
}
