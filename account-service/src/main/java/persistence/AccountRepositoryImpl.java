package persistence;

import core.domain.account.Account;
import core.domain.account.AccountId;
import core.domain.account.AccountRepository;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class AccountRepositoryImpl implements AccountRepository {

    private final Map<AccountId, Account> accounts;

    public AccountRepositoryImpl() {
        this.accounts = new HashMap<>();
    }

    @Override
    public Account find(AccountId accountId) {
        System.out.println("Looking for account with ID: " + accountId);
        Account account = accounts.get(accountId);
        if (account == null) {
            System.out.println("Account not found for ID: " + accountId);
        }
        return account;
    }

    @Override
    public void add(Account account) {
        accounts.put(account.getId(), account);
        System.out.println("Added account with ID: " + account.getId());
    }
    @Override
    public void remove(Account account) {
        accounts.remove(account.getId());
    }

    @Override
    public void delete(AccountId id) {
        accounts.remove(id);
    }
}
