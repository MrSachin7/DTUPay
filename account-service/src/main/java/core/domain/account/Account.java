package core.domain.account;

import core.domain.common.Aggregate;

public class Account extends Aggregate<AccountId> {
    private CprNumber cprNumber;
    private Name name;
    private BankAccountNumber bankAccountNumber;

    private Account(){
    }

    public static Account newAccount(CprNumber cprNumber, Name name, BankAccountNumber bankAccountNumber){
        Account account = new Account();
        account.id = AccountId.newAccountId(); // We assign a new account id to the account
        account.cprNumber = cprNumber;
        account.name = name;
        account.bankAccountNumber = bankAccountNumber;
        return account;
    }

    public CprNumber getCprNumber() {
        return cprNumber;
    }

    public Name getName() {
        return name;
    }

    public BankAccountNumber getBankAccountNumber() {
        return bankAccountNumber;
    }

    public AccountId getId() {
        return id;
    }
}
