package ch.cloudcoins.boundary;

import ch.cloudcoins.CustomRuntimeException;
import ch.cloudcoins.MessageKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomRuntimeExceptionMapper implements ExceptionMapper<CustomRuntimeException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRuntimeExceptionMapper.class);

    @Override
    public Response toResponse(CustomRuntimeException exception) {
        MessageKey messageKey = exception.getMessageKey();

        LOGGER.info("Catching CustomRuntimeException with message: {}", messageKey.getKey());

        return ErrorMapper.getServerError(messageKey);
    }
}
