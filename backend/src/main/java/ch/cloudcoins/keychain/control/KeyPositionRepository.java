package ch.cloudcoins.keychain.control;

import ch.cloudcoins.BaseRepository;
import ch.cloudcoins.keychain.entity.KeyPosition;
import ch.cloudcoins.keychain.entity.Keychain;

public class KeyPositionRepository extends BaseRepository<KeyPosition> {

    public KeyPosition increaseKeyPosition(Keychain keychain, int coinType) {
        KeyPosition position = singleResult(
                createNamedQuery("KeyPosition.findByKeychainAndCoinType")
                        .setParameter("keychainId", keychain.getId())
                        .setParameter("coinType", coinType)
        );
        if (position == null) {
            position = new KeyPosition();
            position.setKeychain(keychain);
            position.setCoinType(coinType);
            position.setIndex(0);
            position.setCustom(false);
            persist(position);
            return position;
        } else {
            position.setIndex(position.getIndex() + 1);
            return merge(position);
        }
    }
}
