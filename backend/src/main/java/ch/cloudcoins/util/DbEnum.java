package ch.cloudcoins.util;

public interface DbEnum<T> {

    String getDbValue();

    T[] getValues();
}
