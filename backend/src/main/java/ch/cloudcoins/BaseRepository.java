package ch.cloudcoins;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * Provides generic functions for CRUD operations on a JPA entity.
 *
 * @param <T> The Entity Type.
 */
public abstract class BaseRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> entityType;

    protected BaseRepository() {
        entityType = Objects.requireNonNull(ReflectionUtil.<T>getActualTypeArguments(getClass(), 0));
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected void flush() {
        this.entityManager.flush();
    }

    /**
     * Creates an entity on the database. The object is updated with the ID that it is created with.
     *
     * @param entity The entity to create
     */
    public void persist(T entity) {
        prePersist(entity);

        entityManager.persist(entity);
    }

    /**
     * Can be overridden in a subclass to check/change the entity before persist.
     */
    protected void prePersist(T entity) {
    }

    /**
     * Updates an entity on the database. A new reference to the updated entity is returned, the old one should be
     * discarded.
     *
     * @param entity The entity to update
     * @return A new reference to the updated object that should be used for further actions.
     */
    public T merge(T entity) {
        preMerge(entity);

        return entityManager.merge(entity);
    }

    /**
     * Can be overridden in a subclass to check/change the entity before merge.
     */
    protected void preMerge(T entity) {
    }

    /**
     * Deletes the entity with the provided id.
     *
     * @param id id of the entity to delete.
     * @return true if the entity to delete was found - otherwise false.
     */
    public boolean remove(long id) {
        T entity = find(id);
        if (entity == null) {
            return false;
        }
        remove(entity);
        return true;
    }

    /**
     * Removes an entity from the database.
     *
     * @param entity The entity to remove.
     */
    public void remove(T entity) {
        preRemove(entity);

        entityManager.remove(entity);
    }

    /**
     * Can be overridden in a subclass to check/change the entity before remove.
     */
    protected void preRemove(T entity) {
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity to look for
     * @return The entity from the database or null if it was not found
     */
    public T find(long id) {
        T entity = entityManager.find(entityType, id);
        return entity;
    }

    /**
     * Returns a type-safe result list which contains all entities of the provided type.
     *
     * @return the type-safe result list.
     */
    public List<T> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityType);
        CriteriaQuery<T> all = query.select(query.from(entityType));
        return resultList(entityManager.createQuery(all));
    }

    /**
     * Returns a type-safe result list of the given query.
     *
     * @param query the query
     * @return the result list
     */
    protected List<T> resultList(TypedQuery<T> query) {
        List<T> entityList = query.getResultList();
        return entityList;
    }

    /**
     * Returns a type-safe single result of the given query or null.
     *
     * @param query Typed query to get result from
     * @return the result or null
     * @throws NonUniqueResultException if more than one result
     */
    protected T singleResult(TypedQuery<T> query) {
        List<T> resultList = resultList(query);

        if (resultList.isEmpty()) {
            return null;
        }

        if (resultList.size() > 1) {
            // maybe the result is a join, so make it distinct.
            Set<T> distinctResult = new HashSet<>(resultList);
            if (distinctResult.size() > 1) {
                throw new NonUniqueResultException("Result for query '" + query + "' must contain exactly one item");
            }
        }

        return resultList.get(0);
    }

    protected EntityGraph<T> getGraph() {
        return entityManager.createEntityGraph(entityType);
    }

    protected T findWithGraph(EntityGraph<T> graph, long id) {
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);

        T entity = entityManager.find(entityType, id, hints);
        return entity;
    }

    protected CriteriaQuery<T> createQuery() {
        return getCriteriaBuilder().createQuery(entityType);
    }

    protected TypedQuery<T> createQuery(CriteriaQuery<T> query) {
        return entityManager.createQuery(query);
    }

    protected Query createNoTypedNamedQuery(String queryName) {
        return entityManager.createNamedQuery(queryName);
    }

    protected TypedQuery<T> createNamedQuery(String queryName) {
        return entityManager.createNamedQuery(queryName, entityType);
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    protected TypedQuery<T> createNamedQueryWithGraph(String queryName, EntityGraph<T> graph) {
        TypedQuery<T> query = createNamedQuery(queryName);
        query.setHint("javax.persistence.loadgraph", graph);
        return query;
    }

    protected Predicate lowercaseLike(From<?, ?> from, String attributeName, String keyword) {
        CriteriaBuilder builder = getCriteriaBuilder();
        return builder.like(
                builder.lower(
                        from.get(attributeName).as(String.class)
                ), "%" + keyword.toLowerCase() + "%"
        );
    }
}
