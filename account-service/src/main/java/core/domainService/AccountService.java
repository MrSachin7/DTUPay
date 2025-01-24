package core.domainService;

import core.domain.account.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public String registerAccount(String firstname, String lastname, String cprNumber, String accountNumber){
        CprNumber cpr = CprNumber.from(cprNumber);
        Name name = Name.from(firstname, lastname);
        BankAccountNumber bankAccountNumber = BankAccountNumber.from(accountNumber);
        Account account = Account.newAccount(cpr, name, bankAccountNumber);

        accountRepository.add(account);
        return account.getId().toString();

    }

    public String retrieveBankAccount(String accountId) throws Exception {
        Account account = findAccount(id);

        if (account == null){
            throw new Exception("Account not found");
        }

        return account.getBankAccountNumber().getValue();
    }

    public void unregisterAccount(String accountId) throws Exception {

        Account account = accountRepository.find(AccountId.from(accountId));

        if (account != null){
            throw new Exception("Account not found");
        }
        accountRepository.delete(AccountId.from(accountId));
    }

    private Account findAccount(String id){
        return accountRepository.find(AccountId.from(id));
    }

    private Account findAccountByCpr(String cpr){
        return accountRepository.findByCpr(CprNumber.from(cpr));
    }
}
