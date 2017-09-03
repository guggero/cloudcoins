package ch.cloudcoins.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ClientErrorExceptionMapper implements ExceptionMapper<ClientErrorException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientErrorExceptionMapper.class);

    @Override
    public Response toResponse(ClientErrorException exception) {

        LOGGER.info("Catching ClientErrorException with message: {}", exception.getMessage());

        return ErrorMapper.mapException(exception);
    }
}
