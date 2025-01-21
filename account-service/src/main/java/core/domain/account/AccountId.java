package core.domain.account;

import java.util.UUID;

public class AccountId {

    private UUID value;

    private AccountId() {
    }

    public static AccountId from(String accountId) {
        AccountId id = new AccountId();
        id.value = UUID.fromString(accountId);
        return id;
    }

    public static AccountId newAccountId() {
        AccountId accountId = new AccountId();
        accountId.value = UUID.randomUUID();
        return accountId;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountId accountId = (AccountId) obj;
        return value.equals(accountId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();  // Use the UUID's hashCode
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
