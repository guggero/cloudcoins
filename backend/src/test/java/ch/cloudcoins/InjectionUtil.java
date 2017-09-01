package ch.cloudcoins;

import java.lang.reflect.Field;

public class InjectionUtil {

    /**
     * Helps to set fields annotated by @Inject
     *
     * @param fieldName        name of the field of the class to set the resource.
     * @param toInjectResource class to inject the given resource.
     * @param toBeInjected     resource to be injected / set.
     * @param <T>              class type of the class to set the resource.
     */
    public static <T> void inject(String fieldName, Class<T> injectResourceClass, Object toInjectResource, Object toBeInjected) {
        try {
            Field field = injectResourceClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(toInjectResource, toBeInjected);
        } catch (Exception e) {
            throw new RuntimeException("Bad injection, check field!", e);
        }
    }
}
