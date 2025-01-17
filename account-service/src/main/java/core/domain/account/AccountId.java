package core.domain.account;
import core.domain.common.Id;
import java.util.UUID;

public class AccountId extends Id {

    private AccountId(){
    }

    public static AccountId newAccountId(){
        AccountId accountId = new AccountId();
        accountId.value = UUID.randomUUID();
        return accountId;
    }
}