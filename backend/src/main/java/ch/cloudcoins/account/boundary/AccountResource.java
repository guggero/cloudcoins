package ch.cloudcoins.account.boundary;

import ch.cloudcoins.account.control.AccountRepository;
import ch.cloudcoins.account.entity.Account;
import ch.cloudcoins.security.boundary.PermitAll;
import ch.cloudcoins.security.control.LoginContextHolder;
import ch.cloudcoins.security.control.TokenService;
import ch.cloudcoins.security.entity.LoginContext;
import ch.cloudcoins.security.entity.Token;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Objects;

import static ch.cloudcoins.EntityResource.*;
import static ch.cloudcoins.MessageKey.ERROR_INVALID_ACCESS;
import static ch.cloudcoins.MessageKey.ERROR_INVALID_INPUT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("/accounts")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private AccountRepository repository;

    @Inject
    private TokenService tokenService;

    @POST
    @PermitAll
    public Response addAccount(@QueryParam("otp") Integer otp, Account account) {
        if (account == null || otp == null) {
            return businessError(ERROR_INVALID_INPUT);
        }

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(account.getOtpAuthKey(), otp);

        if (isCodeValid) {
            repository.persist(account);
            return ok();
        }
        return businessError(ERROR_INVALID_ACCESS);
    }

    @GET
    @Path("/salt")
    @PermitAll
    public Response getSaltByEmail(@QueryParam("email") String email, @QueryParam("otp") Integer otp) {
        if (email == null || email.isEmpty() || otp == null) {
            return entityNotFound();
        }
        Account account = repository.findByEmail(email);
        if (account == null) {
            return entityNotFound();
        }

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(account.getOtpAuthKey(), otp);

        if (isCodeValid) {
            return ok(new HashMap<String, String>() {{
                put("salt", account.getSalt());
            }});
        }
        return businessError(ERROR_INVALID_ACCESS);
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@QueryParam("otp") Integer otp, Account account) {
        if (account == null || account.getEmail() == null || account.getEmail().isEmpty()) {
            return businessError(ERROR_INVALID_INPUT);
        }
        Account dbAccount = repository.findByEmail(account.getEmail());
        if (dbAccount == null || !Objects.equals(dbAccount.getPassword(), account.getPassword())) {
            return businessError(ERROR_INVALID_ACCESS);
        }

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(dbAccount.getOtpAuthKey(), otp);

        if (isCodeValid) {
            Token token = tokenService.createToken(dbAccount);
            return ok(new HashMap<String, String>() {{
                put("token", token.getTokenString());
            }});
        }
        return businessError(ERROR_INVALID_ACCESS);
    }

    @POST
    @Path("/logout")
    public Response logout() {
        LoginContext context = LoginContextHolder.get();
        tokenService.invalidateToken(context.getToken());
        return ok();
    }
}
