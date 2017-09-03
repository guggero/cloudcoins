package ch.cloudcoins.keychain.control;

import ch.cloudcoins.BaseRepository;
import ch.cloudcoins.account.entity.Account;
import ch.cloudcoins.keychain.entity.Keychain;

import java.util.List;

public class KeychainRepository extends BaseRepository<Keychain> {

    public List<Keychain> findByAccount(Account account) {
        return resultList(createNamedQuery("Keychain.findByAccount").setParameter("accountId", account.getId()));
    }
}
