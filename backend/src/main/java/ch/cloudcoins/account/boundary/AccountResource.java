package ch.cloudcoins.account.boundary;

import ch.cloudcoins.account.control.AccountRepository;
import ch.cloudcoins.account.entity.Account;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Objects;

import static ch.cloudcoins.EntityResource.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("/accounts")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class AccountResource {

    @Inject
    private AccountRepository repository;

    @POST
    public Response addAccount(@QueryParam("otp") Integer auth, Account account) {
        if (account == null || auth == null) {
            return businessError();
        }

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(account.getOtpAuthKey(), auth);

        if (isCodeValid) {
            repository.persist(account);
            return ok();
        }
        return businessError();
    }

    @GET
    @Path("/salt")
    public Response getSaltByEmail(@QueryParam("email") String email, @QueryParam("auth") Integer auth) {
        if (email == null || email.isEmpty() || auth == null) {
            return entityNotFound();
        }
        Account account = repository.findByEmail(email);
        if (account == null) {
            return entityNotFound();
        }

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(account.getOtpAuthKey(), auth);

        if (isCodeValid) {
            return ok(new HashMap<String, String>() {{
                put("salt", account.getSalt());
            }});
        }
        return businessError();
    }

    @POST
    @Path("/login")
    public Response login(Account account) {
        if (account == null || account.getEmail() == null || account.getEmail().isEmpty()) {
            return businessError();
        }
        Account dbAccount = repository.findByEmail(account.getEmail());
        if (dbAccount == null || !Objects.equals(dbAccount.getPassword(), account.getPassword())) {
            return businessError();
        }
        return ok(new HashMap<>());
    }
}
