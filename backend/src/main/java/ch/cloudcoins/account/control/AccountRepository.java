package ch.cloudcoins.account.control;

import ch.cloudcoins.BaseRepository;
import ch.cloudcoins.account.entity.Account;

public class AccountRepository extends BaseRepository<Account> {

    public Account findByUsername(String username) {
        return singleResult(createNamedQuery("Account.findByUsername").setParameter("username", username));
    }
}
