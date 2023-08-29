package vn.com.ids.myachef.business.exception.message;

public enum ZaloOAErrorMessage {

    // 400
    CANNOT_AUTHENTICATION_OFFICIAL_ACCOUNT("Cannot authentication Official Account", 400), //

    // 404
    ZALOOA_NOT_FOUND("Not found ZaloOA with id: %s", 404);

    public final String message;
    public final int statusCode;

    private ZaloOAErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
