package ch.cloudcoins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        return Response.ok().build();
    }

    public static Response ok(Object entity) {
        return Response.ok(entity).build();
    }

    public static Response noContent() {
        return Response.status(NO_CONTENT).build();
    }

    public static Response entityNotFound() {
        return createResponse(NOT_FOUND);
    }

    public static Response businessError() {
        return createResponse(BAD_REQUEST, null);
    }

    public static Response businessError(String message) {
        return createResponse(BAD_REQUEST, message);
    }

    public static Response internalServerError() {
        return createResponse(INTERNAL_SERVER_ERROR);
    }

    private static Response createResponse(Response.Status status) {
        return createResponse(status, null);
    }

    private static Response createResponse(Response.Status status, String message) {
        Response.ResponseBuilder responseBuilder = Response.status(status);
        if (message != null) {
            LOGGER.info("Returning response with status" + status + " and message " + message);
            return responseBuilder.entity(message).build();
        }
        LOGGER.info("Returning response with status" + status);
        return responseBuilder.build();
    }
}
