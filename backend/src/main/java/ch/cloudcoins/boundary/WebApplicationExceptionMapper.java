package ch.cloudcoins.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebApplicationExceptionMapper.class);

    @Override
    public Response toResponse(WebApplicationException exception) {
        LOGGER.info("Mapping exception: {}", exception.toString());

        return ErrorMapper.mapException(exception);
    }
}
