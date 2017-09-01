package ch.cloudcoins;

import org.junit.rules.ExternalResource;

import javax.persistence.*;
import java.lang.reflect.Field;

public class PersistenceTestRule extends ExternalResource {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;
    private EntityTransaction transaction;

    private final Object target;

    public PersistenceTestRule(Object target) {
        this.target = target;
    }

    @Override
    protected void before() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("cloudcoins-test");
        }

        entityManager = entityManagerFactory.createEntityManager();

        injectPersistence(target);

        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @Override
    protected void after() {
        transaction.rollback();
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

    private void injectPersistence(Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PersistenceContext.class)) {
                injectFieldValue(field, target, entityManager);
            } else if (field.isAnnotationPresent(PersistenceUnit.class)) {
                injectFieldValue(field, target, entityManagerFactory);
            }
        }
    }

    private void injectFieldValue(Field field, Object target, Object value) {
        if (field.getType().isAssignableFrom(value.getClass())) {
            boolean wasAccessible = field.isAccessible();
            field.setAccessible(true);
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Field not accessible", e);
            } finally {
                field.setAccessible(wasAccessible);
            }
        }
    }
}
