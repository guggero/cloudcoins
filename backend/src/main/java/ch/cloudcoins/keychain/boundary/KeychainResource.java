package ch.cloudcoins.keychain.boundary;

import ch.cloudcoins.keychain.control.KeyPositionRepository;
import ch.cloudcoins.keychain.control.KeychainRepository;
import ch.cloudcoins.keychain.entity.KeyPosition;
import ch.cloudcoins.keychain.entity.Keychain;
import ch.cloudcoins.security.control.LoginContextHolder;
import ch.cloudcoins.security.entity.LoginContext;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.Objects;

import static ch.cloudcoins.EntityResource.*;
import static ch.cloudcoins.MessageKey.ERROR_INVALID_ACCESS;
import static ch.cloudcoins.MessageKey.ERROR_INVALID_INPUT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path("/keychains")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class KeychainResource {

    @Inject
    private KeychainRepository repository;

    @Inject
    private KeyPositionRepository keyPositionRepository;

    @GET
    public Response findAll() {
        LoginContext context = LoginContextHolder.get();
        return ok(repository.findByAccount(context.getAccount()));
    }

    @POST
    public Response createKeychain(Keychain keychain) {
        LoginContext context = LoginContextHolder.get();
        if (keychain == null) {
            return businessError(ERROR_INVALID_INPUT);
        }

        keychain.setAccount(context.getAccount());
        keychain.setCreatedAt(LocalDateTime.now());
        repository.persist(keychain);
        return ok(keychain);
    }

    @DELETE
    @Path("/{keychainId}")
    public Response deleteKeychain(@PathParam("keychainId") Long keychainId) {
        LoginContext context = LoginContextHolder.get();

        if (keychainId == null) {
            return businessError(ERROR_INVALID_INPUT);
        }

        Keychain keychain = repository.find(keychainId);
        if (keychain == null) {
            return entityNotFound();
        } else if (!Objects.equals(keychain.getAccount().getId(), context.getAccount().getId())) {
            return businessError(ERROR_INVALID_ACCESS);
        }

        repository.remove(keychain);
        return ok();
    }

    @POST
    @Path("/{keychainId}/positions/{coinType}/increase")
    public Response increaseKeyPosition(@PathParam("keychainId") Long keychainId,
                                        @PathParam("coinType") Integer coinType) {
        LoginContext context = LoginContextHolder.get();

        if (keychainId == null || coinType == null) {
            return businessError(ERROR_INVALID_INPUT);
        }

        Keychain keychain = repository.find(keychainId);
        if (keychain == null) {
            return entityNotFound();
        } else if (!Objects.equals(keychain.getAccount().getId(), context.getAccount().getId())) {
            return businessError(ERROR_INVALID_ACCESS);
        }

        KeyPosition position = keyPositionRepository.increaseKeyPosition(keychain, coinType);
        return ok(position);
    }

    @POST
    @Path("/{keychainId}/positions/{coinType}/custom")
    public Response addCustomPosition(@PathParam("keychainId") Long keychainId,
                                      @PathParam("coinType") Integer coinType,
                                      @QueryParam("index") Integer index) {
        LoginContext context = LoginContextHolder.get();

        if (keychainId == null || coinType == null || index == null) {
            return businessError(ERROR_INVALID_INPUT);
        }

        Keychain keychain = repository.find(keychainId);
        if (keychain == null) {
            return entityNotFound();
        } else if (!Objects.equals(keychain.getAccount().getId(), context.getAccount().getId())) {
            return businessError(ERROR_INVALID_ACCESS);
        }

        KeyPosition position = new KeyPosition();
        position.setKeychain(keychain);
        position.setCoinType(coinType);
        position.setIndex(index);
        position.setCustom(true);
        keyPositionRepository.persist(position);
        return ok(position);
    }
}
