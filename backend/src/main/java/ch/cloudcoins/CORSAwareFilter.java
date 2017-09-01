package ch.cloudcoins;

import ch.cloudcoins.environment.control.EnvironmentService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import static ch.cloudcoins.environment.EnvironmentVariables.CORS_AWARE_FILTER;

/**
 * Filter which enables cross origin resource sharing (CORS) for the exposed REST API.
 */
@Provider
@PreMatching
public class CORSAwareFilter implements ContainerResponseFilter {

    @Inject
    private EnvironmentService environmentService;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        if (Boolean.valueOf(environmentService.getValue(CORS_AWARE_FILTER))) {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        }
    }
}
