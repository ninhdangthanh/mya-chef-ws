package vn.com.ids.myachef.business.exception.message;

public enum UtilsErrorMessage {

    // AES 
    INVALID_DECRYPT_TEXT("Invalid decrypt text", 400),//
    INVALID_ENCRYPT_TEXT("Invalid encrypt text", 400);

    public final String message;
    public final int statusCode;

    private UtilsErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
