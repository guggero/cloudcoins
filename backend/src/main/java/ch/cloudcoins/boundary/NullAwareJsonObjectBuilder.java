package ch.cloudcoins.boundary;

import javax.json.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import static javax.json.JsonValue.NULL;

public class NullAwareJsonObjectBuilder implements JsonObjectBuilder {
    private final JsonObjectBuilder decoratedBuilder;

    private NullAwareJsonObjectBuilder(JsonObjectBuilder decoratedBuilder) {
        if (decoratedBuilder == null) {
            throw new NullPointerException("decoratedBuilder may not be null");
        }
        this.decoratedBuilder = decoratedBuilder;
    }

    public static JsonObjectBuilder createObjectBuilder() {
        return new NullAwareJsonObjectBuilder(Json.createObjectBuilder());
    }

    @Override
    public JsonObjectBuilder add(String name, JsonValue value) {
        return decoratedBuilder.add(name, value != null ? value : NULL);
    }

    @Override
    public JsonObjectBuilder add(String name, String value) {
        return decoratedBuilder.add(name, value);
    }

    @Override
    public JsonObjectBuilder add(String name, BigInteger value) {
        return decoratedBuilder.add(name, value);
    }

    @Override
    public JsonObjectBuilder add(String name, BigDecimal value) {
        return decoratedBuilder.add(name, value);
    }

    @Override
    public JsonObjectBuilder add(String name, int value) {
        return decoratedBuilder.add(name, value);
    }

    @Override
    public JsonObjectBuilder add(String name, long value) {
        return decoratedBuilder.add(name, value);
    }

    @Override
    public JsonObjectBuilder add(String name, double value) {
        return decoratedBuilder.add(name, value);
    }

    @Override
    public JsonObjectBuilder add(String name, boolean value) {
        return decoratedBuilder.add(name, value);
    }

    @Override
    public JsonObjectBuilder addNull(String name) {
        return decoratedBuilder.addNull(name);
    }

    @Override
    public JsonObjectBuilder add(String name, JsonObjectBuilder builder) {
        return decoratedBuilder.add(name, builder);
    }

    @Override
    public JsonObjectBuilder add(String name, JsonArrayBuilder builder) {
        return decoratedBuilder.add(name, builder);
    }

    @Override
    public JsonObject build() {
        return decoratedBuilder.build();
    }
}
