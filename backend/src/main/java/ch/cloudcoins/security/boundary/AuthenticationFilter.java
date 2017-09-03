package ch.cloudcoins.security.boundary;

import ch.cloudcoins.security.control.LoginContextHolder;
import ch.cloudcoins.security.control.TokenService;
import ch.cloudcoins.security.entity.LoginContext;
import ch.cloudcoins.security.entity.Token;
import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.transaction.UserTransaction;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static ch.cloudcoins.MessageKey.ERROR_INVALID_INPUT;
import static ch.cloudcoins.security.control.TransactionHelper.commitTransaction;
import static ch.cloudcoins.security.control.TransactionHelper.startTransaction;
import static javax.ws.rs.HttpMethod.OPTIONS;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String TOKEN_HEADER_PREFIX = "Bearer ";

    private TokenService tokenService;

    public AuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // ignore OPTIONS requests, there won't be any tokens set
        if (OPTIONS.equals(requestContext.getMethod())) {
            requestContext.abortWith(Response.ok().build());
            return;
        }

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        validateSession(authorizationHeader, true);
    }

    public void validateSession(String authorizationHeader, boolean refreshSession) {
        validateAuthorizationHeader(authorizationHeader);

        String tokenString = authorizationHeader.substring(TOKEN_HEADER_PREFIX.length()).trim();
        Token token = tokenService.validateToken(tokenString);

        String email = token.getAccount().getEmail();
        LoginContext context = new LoginContext(token.getAccount(), email, token.getTokenString());
        LoginContextHolder.set(context);

        // update email in MDC so it will be logged for each request
        MDC.put("email", email);

        if (refreshSession) {
            UserTransaction userTransaction = startTransaction();
            if (userTransaction != null) {
                tokenService.refreshToken(token);
                commitTransaction(userTransaction);
            }
        }
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_HEADER_PREFIX)) {
            throw new ClientErrorException(ERROR_INVALID_INPUT.getKey(), UNAUTHORIZED);
        }
    }
}
