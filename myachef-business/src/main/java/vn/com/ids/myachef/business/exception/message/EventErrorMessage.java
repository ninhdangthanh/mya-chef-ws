package vn.com.ids.myachef.business.exception.message;

public enum EventErrorMessage {

    // 404
    EVENT_NOT_FOUND("Not found Event with id: %s", 404);

    public final String message;
    public final int statusCode;

    private EventErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
