package ch.cloudcoins;

public enum MessageKey {
    ERROR_ENTITY_NOT_FOUND("ch.cloudcoins.errors.entityNotFound"),
    ERROR_ENTITY_MISSING("ch.cloudcoins.errors.entityMissing"),
    ERROR_INVALID_INPUT("ch.cloudcoins.errors.invalidInput"),
    ERROR_INVALID_STATE("ch.cloudcoins.errors.invalidState"),
    ERROR_INVALID_ACCESS("ch.cloudcoins.errors.invalidAccess"),
    ERROR_CONCURRENT_UPDATE("ch.cloudcoins.errors.concurrentUpdateOccured"),
    ERROR_INTERNAL_SERVER_ERROR("ch.cloudcoins.errors.internalServerError"),
    ERROR_USERNAME_EXISTS("ch.cloudcoins.errors.usernameExists");

    private final String key;

    MessageKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
