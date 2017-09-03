package ch.cloudcoins;

public class CustomRuntimeException extends RuntimeException {

    private final MessageKey messageKey;

    public CustomRuntimeException(MessageKey messageKey, Throwable cause) {
        super(messageKey.getKey(), cause);
        this.messageKey = messageKey;
    }

    public CustomRuntimeException(MessageKey messageKey) {
        super(messageKey.getKey());
        this.messageKey = messageKey;
    }

    public MessageKey getMessageKey() {
        return messageKey;
    }
}
