package ch.cloudcoins.keychain.entity;

import ch.cloudcoins.account.entity.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
public class Keychain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "keychain_seq")
    @SequenceGenerator(name = "keychain_seq", sequenceName = "keychain_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(length = 1024)
    private String key;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne
    @JsonBackReference("account-keychain")
    private Account account;

    @OneToMany(mappedBy = "keychain", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    @JsonManagedReference("keychain-keyposition")
    private Set<KeyPosition> positions = new HashSet<>();

    public Keychain() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<KeyPosition> getPositions() {
        return positions;
    }

    public void setPositions(Set<KeyPosition> positions) {
        this.positions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Keychain keychain = (Keychain) o;
        return Objects.equals(id, keychain.id) &&
                Objects.equals(account, keychain.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account);
    }
}
