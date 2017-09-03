package ch.cloudcoins.security.boundary;

import ch.cloudcoins.security.control.LoginContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class ContextResponseFilter implements ContainerResponseFilter {
    private static final Logger LOG = LoggerFactory.getLogger(ContextResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        LOG.trace("Clearing context: " + LoginContextHolder.get());
        LoginContextHolder.clearContext();
    }
}
