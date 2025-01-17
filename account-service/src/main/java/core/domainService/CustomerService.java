package core.domainService;

import core.domain.account.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerService {

    private final AccountRepository accountRepository;

    public CustomerService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public String registerCustomer(String firstname, String lastname, String cprNumber, String accountNumber){
        CprNumber cpr = CprNumber.from(cprNumber);
        Name name = Name.from(firstname, lastname);
        BankAccountNumber bankAccountNumber = BankAccountNumber.from(accountNumber);
        Account account = Account.newAccount(cpr, name, bankAccountNumber);

        accountRepository.add(account);
        return account.getId().getValue();

    }
}
