package vn.com.ids.myachef.business.exception.message;

public enum VersionErrorMessage {

    // 400
    VERSION_EXISTED("%s is existed", 400);

    public final String message;
    public final int statusCode;

    private VersionErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
