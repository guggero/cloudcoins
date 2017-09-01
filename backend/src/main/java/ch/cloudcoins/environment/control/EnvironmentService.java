package ch.cloudcoins.environment.control;

import ch.cloudcoins.environment.EnvironmentVariables;

/**
 * Service that provides access to environment variables.
 */
public class EnvironmentService {

    public String getValue(EnvironmentVariables variable) {
        return System.getenv(variable.name());
    }
}
