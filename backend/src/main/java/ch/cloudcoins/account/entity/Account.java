package ch.cloudcoins.account.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
}
