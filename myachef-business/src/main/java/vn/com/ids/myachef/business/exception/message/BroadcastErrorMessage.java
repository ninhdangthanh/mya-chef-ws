package vn.com.ids.myachef.business.exception.message;

public enum BroadcastErrorMessage {

    // 400
    BROADCAST_IS_FULL("Broadcast your workspace is full", 400), //

    // 404
    BROADCAST_NOT_FOUND("Not found Broadcast with id: %s", 404);

    public final String message;
    public final int statusCode;

    private BroadcastErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}