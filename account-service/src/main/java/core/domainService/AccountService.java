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
        return account.getId().getValue();

    }

    public String retrieveBankAccount(String id) throws Exception {
        Account account = accountRepository.find(AccountId.from(id));

        if (account == null){
            throw new Exception("Merchant not found");
        }

        return account.getBankAccountNumber().getValue();
    }
}
