package ch.cloudcoins.boundary;

import ch.cloudcoins.CustomRuntimeException;
import ch.cloudcoins.MessageKey;
import org.slf4j.MDC;

import javax.json.*;
import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Set;

import static ch.cloudcoins.MessageKey.ERROR_CONCURRENT_UPDATE;
import static ch.cloudcoins.MessageKey.ERROR_INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;

public class ErrorMapper {

    public static final String MESSAGE_KEY = "messageKey";
    public static final String MESSAGE_TEXT = "messageText";
    public static final String MESSAGE_ARGUMENTS = "messageArguments";
    public static final String CORRELATION_ID = "correlationId";

    private final JsonObjectBuilder errors;
    private final JsonArrayBuilder jsonArrayBuilder;

    private ErrorMapper() {
        jsonArrayBuilder = Json.createArrayBuilder();
        errors = NullAwareJsonObjectBuilder.createObjectBuilder();
    }

    public static ErrorMapper create() {
        return new ErrorMapper();
    }

    private JsonArray toJsonArray(Object... messageParameters) {
        JsonArrayBuilder params = Json.createArrayBuilder();
        for (Object messageParameter : messageParameters) {
            if (messageParameter != null) {
                params.add(messageParameter.toString());
            }
        }
        return params.build();
    }

    public ErrorMapper addMessageKey(String messageKey, Object... messageParameters) {
        JsonObject error = NullAwareJsonObjectBuilder.createObjectBuilder()
                .add(MESSAGE_KEY, messageKey)
                .add(MESSAGE_ARGUMENTS, toJsonArray(messageParameters))
                .add(CORRELATION_ID, getCorrelationId())
                .build();
        jsonArrayBuilder.add(error);
        return this;
    }

    public ErrorMapper addMessageText(String messageText, Object... messageParameters) {
        JsonObject error = NullAwareJsonObjectBuilder.createObjectBuilder()
                .add(MESSAGE_TEXT, messageText)
                .add(MESSAGE_ARGUMENTS, toJsonArray(messageParameters))
                .add(CORRELATION_ID, getCorrelationId())
                .build();
        jsonArrayBuilder.add(error);
        return this;
    }

    private String getCorrelationId() {
        Object correlationId = MDC.get("correlationId");
        if (correlationId != null) {
            return (String) correlationId;
        } else {
            return "<<empty>>";
        }
    }

    public JsonObject build() {
        return errors.add("errors", jsonArrayBuilder).build();
    }

    public static Throwable getCause(Throwable e) {
        return e.getCause() != null ? getCause(e.getCause()) : e;
    }

    public static Response mapException(Throwable cause) {
        if (cause instanceof ConstraintViolationException) {
            ConstraintViolationException root = (ConstraintViolationException) cause;
            Set<ConstraintViolation<?>> violations = root.getConstraintViolations();

            ErrorMapper errorMapper = ErrorMapper.create();
            for (ConstraintViolation<?> violation : violations) {
                String messageTemplate = violation.getMessageTemplate();
                if (messageTemplate != null) {
                    // removing characters '{' and '}' from string
                    errorMapper.addMessageKey(messageTemplate.replaceAll("[\\{\\}]", ""), String.valueOf(violation.getInvalidValue()));
                }
            }
            return Response.status(BAD_REQUEST).type(APPLICATION_JSON).entity(errorMapper.build()).build();
        }

        if (cause instanceof OptimisticLockException) {
            JsonObject error = ErrorMapper.create().addMessageKey(ERROR_CONCURRENT_UPDATE.getKey()).build();
            return Response.status(CONFLICT).entity(error).build();
        }

        if (cause instanceof ClientErrorException) {
            ClientErrorException ex = (ClientErrorException) cause;
            JsonObject error = ErrorMapper.create().addMessageKey(ex.getMessage()).build();
            return Response.status(ex.getResponse().getStatus()).type(APPLICATION_JSON).entity(error).build();
        }

        if (cause instanceof WebApplicationException) {
            return ((WebApplicationException) cause).getResponse();
        }

        if (cause instanceof CustomRuntimeException) {
            MessageKey messageKey = ((CustomRuntimeException) cause).getMessageKey();
            return getServerError(messageKey);
        }

        // default handler
        return getServerError(ERROR_INTERNAL_SERVER_ERROR);
    }

    public static Response getServerError(MessageKey messageKey) {
        JsonObject error = ErrorMapper.create().addMessageKey(messageKey.getKey()).build();
        return Response.serverError().type(APPLICATION_JSON).entity(error).build();
    }
}
