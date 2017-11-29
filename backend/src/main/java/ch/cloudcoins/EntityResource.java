package ch.cloudcoins;

import ch.cloudcoins.boundary.ErrorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ch.cloudcoins.MessageKey.ERROR_ENTITY_NOT_FOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

/**
 * Abstract base class that provides support for CRUD operations on entities.
 *
 * @param <T> The Entity Type.
 */
public abstract class EntityResource<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityResource.class);

    private final Class<T> entityType;

    protected abstract BaseRepository<T> getRepository();

    protected EntityResource() {
        entityType = Objects.requireNonNull(ReflectionUtil.<T>getActualTypeArguments(getClass(), 0));
    }

    public Class<T> getEntityType() {
        return entityType;
    }

    @GET
    public Response findAll(@Context UriInfo uriInfo) {
        LOGGER.debug("Finding all {}s", entityType.getSimpleName());
        List<T> entities = doFindAll(uriInfo.getQueryParameters());
        return ok(entities != null ? entities : new ArrayList<>());
    }

    /**
     * Can be overridden in a subclass to change the default loading strategy.
     *
     * @return A List of entities T.
     */
    protected List<T> doFindAll(MultivaluedMap<String, String> queryParams) {
        return getRepository().findAll();
    }

    @GET
    @Path("/{id: \\d+}")
    public Response findById(@PathParam("id") Long id, @Context UriInfo uriInfo) {
        LOGGER.debug("Finding {} with id {}", entityType.getSimpleName(), id);
        T entity = doFindById(id, uriInfo.getQueryParameters());
        if (entity == null) {
            return entityNotFound();
        }
        return ok(entity);
    }

    /**
     * Can be overridden in a subclass to change the default loading strategy.
     *
     * @return the entity for the provided id. Null if the entity was not found.
     */
    protected T doFindById(Long id, MultivaluedMap<String, String> queryParams) {
        return getRepository().find(id);
    }

    @OPTIONS
    public Response options() {
        return ok();
    }

    public static Response ok() {
        return noContent();
    }

    public static Response ok(Object entity) {
        return Response.ok(entity).build();
    }

    public static Response noContent() {
        return Response.status(NO_CONTENT).build();
    }

    public static Response entityNotFound() {
        return createErrorResponse(NOT_FOUND, ERROR_ENTITY_NOT_FOUND);
    }

    public static Response businessError(MessageKey messageKey, Object... values) {
        return createErrorResponse(BAD_REQUEST, messageKey, values);
    }

    public static Response internalServerError(MessageKey messageKey) {
        return createErrorResponse(INTERNAL_SERVER_ERROR, messageKey);
    }

    private static Response createErrorResponse(Response.Status status, MessageKey messageKey, Object... values) {
        String message = messageKey.getKey();
        String statusText = status.getStatusCode() + " (" + status.getReasonPhrase() + ")";
        LOGGER.info("Returning " + statusText + " error with key '{}' and values {}.", message, Arrays.toString(values));
        JsonObject error = ErrorMapper.create().addMessageKey(message, values).build();
        return Response.status(status).type(APPLICATION_JSON).entity(error).build();
    }
}
