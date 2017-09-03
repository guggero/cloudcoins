package ch.cloudcoins.security.control;

import ch.cloudcoins.account.entity.Account;
import ch.cloudcoins.security.entity.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static ch.cloudcoins.MessageKey.ERROR_INVALID_STATE;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Singleton
@Startup
public class TokenService {

    private static final int TOKEN_LENGTH = 32;
    private static final String TOKEN_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Logger LOG = LoggerFactory.getLogger(TokenService.class);

    @Inject
    private TokenRepository repository;

    // use Random as member and to ensure randomness of nextInt()
    // SecureRandom is thread safe so it's fine to keep as member
    private final Random random;

    public TokenService() {
        random = new SecureRandom();
    }

    public Token createToken(Account account) {
        Token token = new Token(generateRandomString(TOKEN_LENGTH), account);
        repository.persist(token);
        LOG.debug("Created new {} for {}", token, account);
        return token;
    }

    /**
     * Throws a NotAuthorizedException if the token with the provided string was not found in the database or is
     * expired.
     *
     * @param tokenString token to validate.
     */
    public Token validateToken(String tokenString) {
        Token token = repository.findByTokenString(tokenString);
        return validateToken(token);
    }

    /**
     * Throws a NotAuthorizedException if the token with the provided string was not found in the database or is
     * expired.
     *
     * @param token token to validate.
     */
    public Token validateToken(Token token) {
        if (token == null || token.getValidUntil() == null || token.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new ClientErrorException(ERROR_INVALID_STATE.getKey(), UNAUTHORIZED);
        }
        if (token.getAccount() == null) {
            throw new IllegalStateException("Account on token cannot be null");
        }
        return token;
    }

    public Token refreshToken(Token token) {
        token.refreshValidUntil();
        return repository.merge(token);
    }

    public void invalidateToken(String token) {
        Token entity = repository.findByTokenString(token);
        invalidateToken(entity);
    }

    public void invalidateToken(Token token) {
        repository.remove(token);
        LOG.debug("Invalidated {}", token);
    }

    @Schedule(hour = "*", minute = "*", persistent = false) // call every minute, must run on every cluster instance, so persistent = false
    @PostConstruct // cache will be initialized once after creation of singleton bean
    public void removeOldTokens() {
        List<Token> allTokens = repository.findAll();
        allTokens.forEach(t -> {
            try {
                validateToken(t);
            } catch (ClientErrorException e) {
                invalidateToken(t);
            }
        });
    }

    public void removeAccountTokens(Long accountId) {
        List<Token> allTokens = repository.findAll();
        allTokens.forEach(t -> {
            if (Objects.equals(t.getAccount().getId(), accountId)) {
                invalidateToken(t);
            }
        });
    }

    public String generateRandomString(int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append(TOKEN_CHARACTERS.charAt(random.nextInt(TOKEN_CHARACTERS.length())));
        }
        return s.toString();
    }
}
