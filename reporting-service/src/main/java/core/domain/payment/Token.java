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

    public static Token from(String value) {
        Token token = new Token();
        token.value = value;
        return token;
    }
}
