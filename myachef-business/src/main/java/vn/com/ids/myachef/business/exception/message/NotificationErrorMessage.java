package vn.com.ids.myachef.business.exception.message;

public enum NotificationErrorMessage {

    // 400
    WARNING_PERCENT("Percent must <= 100", 400),

    // 404
    NOTIFICATION_NOT_FOUND("Not found Notification with id: %s", 404);

    public final String message;
    public final int statusCode;

    private NotificationErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
