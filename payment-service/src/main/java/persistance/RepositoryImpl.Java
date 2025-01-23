package persistence;

import core.domain.account.Account;
import core.domain.account.AccountId;
import core.domain.account.AccountRepository;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class RepositoryImpl implements AccountRepository {

    private final Map<AccountId, Account> accounts;

    public RepositoryImpl() {
        this.accounts = new HashMap<>();
    }

    @Override
    public Account find(AccountId accountId) {
        return accounts.get(accountId);
    }
    @Override
    public void add(Account account) {
        accounts.put(account.getId(), account);

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
