package ch.cloudcoins.account.boundary;

import ch.cloudcoins.account.control.AccountRepository;
import ch.cloudcoins.account.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private AccountRepository repository;

    @POST
    public Response addAccount(Account account) {
        if (account == null) {
            return businessError();
        }
        repository.persist(account);
        return ok();
    }

    @GET
    @Path("/salt")
    public Response getSaltByEmail(@QueryParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return entityNotFound();
        }
        Account account = repository.findByEmail(email);
        if (account == null) {
            return entityNotFound();
        }

        return ok(new HashMap<String, String>() {{
            put("salt", account.getSalt());
        }});
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
