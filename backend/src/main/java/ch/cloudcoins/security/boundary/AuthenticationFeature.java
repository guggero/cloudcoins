package ch.cloudcoins.security.boundary;

import ch.cloudcoins.security.control.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

@Provider
public class AuthenticationFeature implements DynamicFeature {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFeature.class);

    @Inject
    private TokenService tokenService;

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (!isPermitAll(resourceInfo)) {
            LOG.debug("Enabling AuthenticationFilter for " + resourceInfo.getResourceClass() + " " + resourceInfo.getResourceMethod());
            context.register(new AuthenticationFilter(tokenService));
        } else {
            LOG.debug("Disabling AuthenticationFilter for " + resourceInfo.getResourceClass() + " " + resourceInfo.getResourceMethod());
        }
    }

    boolean isPermitAll(ResourceInfo resourceInfo) {
        Method resourceMethod = resourceInfo.getResourceMethod();
        Class<?> resourceClass = resourceInfo.getResourceClass();
        if (resourceMethod == null || resourceClass == null) {
            return false;
        }

        return resourceClass.isAnnotationPresent(PermitAll.class) || resourceMethod.isAnnotationPresent(PermitAll.class);
    }
}
