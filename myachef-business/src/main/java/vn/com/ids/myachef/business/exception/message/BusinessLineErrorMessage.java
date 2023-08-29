package vn.com.ids.myachef.business.exception.message;

public enum BusinessLineErrorMessage {

    // 404
    BUSINESS_NOT_FOUND("Not found Business with id: %s", 404);

    public final String message;
    public final int statusCode;

    private BusinessLineErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
