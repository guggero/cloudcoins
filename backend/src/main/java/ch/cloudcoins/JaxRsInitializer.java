package ch.cloudcoins;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Initializes JAX-RS for all resources in the application.
 */
@ApplicationPath("api")
public class JaxRsInitializer extends Application {
}
