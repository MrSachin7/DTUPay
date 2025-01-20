package core.domain.account;

import core.domain.common.Repository;


public interface AccountRepository extends Repository<Account, AccountId> {
    void delete(AccountId id);
}
