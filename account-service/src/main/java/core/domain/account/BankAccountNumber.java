package core.domain.account;

import core.domain.common.ValueObject;

public class BankAccountNumber extends ValueObject {
    private String value;

    private BankAccountNumber() {
    }

    public static BankAccountNumber from(String value) {
        BankAccountNumber bankAccountNumber = new BankAccountNumber();
        bankAccountNumber.value = value;
        return bankAccountNumber;
    }

    public String getValue() {
        return value;
    }

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{value};
    }
}
