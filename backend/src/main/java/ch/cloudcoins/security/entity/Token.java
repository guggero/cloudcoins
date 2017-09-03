package ch.cloudcoins.security.entity;

import ch.cloudcoins.account.entity.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "tokenString"))
public class Token {

    public static final long SESSION_TIMEOUT_MINUTES = 60;
    public static final long SESSION_MAX_AGE_HOURS = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @SequenceGenerator(name = "token_seq", sequenceName = "token_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private String tokenString;

    @NotNull
    @ManyToOne
    private Account account;

    @NotNull
    private LocalDateTime creationTime;

    @NotNull
    private LocalDateTime validUntil;

    private Token() {
        creationTime = LocalDateTime.now();
        refreshValidUntil();
    }

    public Token(String tokenString, Account account) {
        this.tokenString = tokenString;
        this.account = account;
        creationTime = LocalDateTime.now();
        refreshValidUntil();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getValidUntil() {
        LocalDateTime maxAge = getMaxAge();

        // a token can never be older than the max age, even if the session is still be kept alive
        if (maxAge.isBefore(validUntil)) {
            return maxAge;
        }
        return validUntil;
    }

    public void refreshValidUntil() {
        this.validUntil = LocalDateTime.now().plusMinutes(SESSION_TIMEOUT_MINUTES);
    }

    public LocalDateTime getMaxAge() {
        return creationTime.plusHours(SESSION_MAX_AGE_HOURS);
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Token{" + "id=" + id + ", tokenString='" + tokenString + '\'' + ", validUntil=" + validUntil + '}';
    }
}
