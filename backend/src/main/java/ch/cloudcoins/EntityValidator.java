package ch.cloudcoins;


import javax.validation.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * Helper bean that validates an item using JSR 303 bean validation.
 */
public class EntityValidator {
    private static final Set<String> IGNORED_PROPERTIES = new HashSet<>(asList("changedOn", "changedBy", "version"));

    /**
     * Performs the JSR 303 bean validation except on the attributes contained in IGNORED_PROPERTIES.
     */
    public void validate(Object o) throws ConstraintViolationException {
        ValidatorFactory buildDefaultValidatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = buildDefaultValidatorFactory.getValidator();
        Set<ConstraintViolation<?>> violations = convert(validator.validate(o));
        for (Iterator<ConstraintViolation<?>> i = violations.iterator(); i.hasNext(); ) {
            ConstraintViolation<?> violation = i.next();
            String path = pathString(violation.getPropertyPath());
            if (IGNORED_PROPERTIES.contains(path)) {
                i.remove();
            }
        }
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public String pathString(Path path) {
        StringBuilder builder = new StringBuilder();
        for (Path.Node node : path) {
            if (node.getName() != null) {
                if (builder.length() > 0) {
                    builder.append(".");
                }
                builder.append(node.getName());
            }
        }
        return builder.toString();
    }

    private Set<ConstraintViolation<?>> convert(Set<ConstraintViolation<Object>> violations) {
        return violations.stream().collect(Collectors.toSet());
    }
}
