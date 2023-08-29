package vn.com.ids.myachef.business.exception.message;

public enum ZaloNotificationErrorMessage {

    // 404
    ZALONOTIFICATION_NOT_FOUND("Not found ZaloNotification with id: %s", 404); //

    public final String message;
    public final int statusCode;

    private ZaloNotificationErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
