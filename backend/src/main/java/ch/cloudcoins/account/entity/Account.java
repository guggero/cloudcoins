package ch.cloudcoins.account.entity;

import ch.cloudcoins.keychain.entity.Keychain;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    @Column(length = 32)
    private String salt;

    @NotNull
    private String password;

    @NotNull
    private String otpAuthKey;

    @OneToMany(mappedBy = "account")
    @JsonManagedReference("account-keychain")
    private Set<Keychain> keychains = new HashSet<>();

    public Account() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtpAuthKey() {
        return otpAuthKey;
    }

    public void setOtpAuthKey(String otpAuthKey) {
        this.otpAuthKey = otpAuthKey;
    }

    public Set<Keychain> getKeychains() {
        return keychains;
    }

    public void setKeychains(Set<Keychain> keychains) {
        this.keychains = keychains;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(email, account.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
