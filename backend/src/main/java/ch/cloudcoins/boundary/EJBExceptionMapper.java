package ch.cloudcoins.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EJBExceptionMapper implements ExceptionMapper<EJBException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EJBExceptionMapper.class);

    @Override
    public Response toResponse(EJBException exception) {
        Throwable cause = ErrorMapper.getCause(exception);

        LOGGER.info("Mapping exception {} with cause: {}", exception.getClass().getName(), cause);

        return ErrorMapper.mapException(cause);
    }
}
