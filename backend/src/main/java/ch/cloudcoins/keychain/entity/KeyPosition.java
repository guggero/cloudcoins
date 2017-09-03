package ch.cloudcoins.keychain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class KeyPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "keyposition_seq")
    @SequenceGenerator(name = "keyposition_seq", sequenceName = "keyposition_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonBackReference("keychain-keyposition")
    private Keychain keychain;

    @NotNull
    private Integer coinType;

    @NotNull
    private Integer index;

    public KeyPosition() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Keychain getKeychain() {
        return keychain;
    }

    public void setKeychain(Keychain keychain) {
        this.keychain = keychain;
    }

    public Integer getCoinType() {
        return coinType;
    }

    public void setCoinType(Integer coinType) {
        this.coinType = coinType;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyPosition that = (KeyPosition) o;
        return Objects.equals(keychain, that.keychain) &&
                Objects.equals(coinType, that.coinType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keychain, coinType);
    }
}
