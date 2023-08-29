package vn.com.ids.myachef.business.exception.message;

public enum PostErrorMessage {

    // 400
    COVER_NULL("Cover is null", 400), //
    IMAGEBASE64_INVALID("your imagebase64 invalid", 400),

    // 404
    POST_NOT_FOUND("Not found Post with id: %s", 404);

    public final String message;
    public final int statusCode;

    private PostErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
