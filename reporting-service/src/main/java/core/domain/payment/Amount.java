package core.domain.payment;

import core.domain.common.ValueObject;

public class Amount extends ValueObject {
    private double value;

    private Amount() {
    }

    public static Amount from(double value) {
        Amount amount = new Amount();
        amount.value = value;
        return amount;
    }

    public double getValue() {
        return value;
    }

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{value};
    }
}
