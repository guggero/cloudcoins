package ch.cloudcoins.account.control;

import ch.cloudcoins.BaseRepository;
import ch.cloudcoins.account.entity.Account;

public class AccountRepository extends BaseRepository<Account> {

    public Account findByEmail(String email) {
        return singleResult(createNamedQuery("Account.findByEmail").setParameter("email", email));
    }
}
