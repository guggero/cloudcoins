package ch.cloudcoins.util;

import javax.persistence.AttributeConverter;

/**
 * Converts between Enum and database value.
 *
 * @param <T> The Enum type.
 */
public class DbValueConverter<T extends DbEnum> implements AttributeConverter<T, String> {

    private String type;

    protected T[] values;

    public DbValueConverter(String type, T[] values) {
        this.type = type;
        this.values = values;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getDbValue();
    }

    @Override
    public T convertToEntityAttribute(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        for (T value : values) {
            if (value.getDbValue().equals(databaseValue)) {
                return value;
            }
        }

        throw new IllegalStateException("Unknown " + type + " database value: " + databaseValue);
    }
}
