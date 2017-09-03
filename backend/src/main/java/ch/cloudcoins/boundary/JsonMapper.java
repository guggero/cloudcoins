package ch.cloudcoins.boundary;

import javax.json.*;
import java.util.ArrayList;
import java.util.Collection;

public abstract class JsonMapper<T> {

    public abstract JsonObject map(T entity);

    public abstract T map(JsonObject jsonObject);

    public JsonArray map(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        entities.forEach(entity -> arrayBuilder.add(map(entity)));
        return arrayBuilder.build();
    }

    public Collection<T> map(JsonArray jsonArray) {
        Collection<T> collection = new ArrayList<>();
        if (jsonArray == null || jsonArray.isEmpty()) {
            return collection;
        }
        jsonArray.forEach(jsonValue -> collection.add(map((JsonObject) jsonValue)));
        return collection;
    }

    protected Long getLong(JsonObject json, String attributeName) {
        if (json == null) {
            return null;
        }
        JsonNumber jsonNumber = json.getJsonNumber(attributeName);
        if (jsonNumber == null) {
            return null;
        }
        return jsonNumber.longValue();
    }

    protected String getString(JsonObject json, String attributeName) {
        if (json == null) {
            return null;
        }
        return json.getString(attributeName, null);
    }
}
