package ch.cloudcoins.security.entity;

import ch.cloudcoins.account.entity.Account;

import java.util.Objects;

public final class LoginContext {

    private final Account account;
    private final String username;
    private final String token;

    public LoginContext(Account account, String username, String token) {
        this.account = account;
        this.username = username;
        this.token = token;
    }

    public Account getAccount() {
        return account;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginContext that = (LoginContext) o;
        return Objects.equals(account, that.account) &&
                Objects.equals(username, that.username) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, username, token);
    }

    @Override
    public String toString() {
        return "LoginContext{" +
                "account=" + account +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
