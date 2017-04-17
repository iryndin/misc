package net.iryndin.mtapi;

/**
 * @author iryndin
 * @since 17/04/17
 */
public class MTException extends RuntimeException {
    private final int errorCode;

    public MTException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
