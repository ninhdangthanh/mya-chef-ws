package vn.com.ids.myachef.business.exception.message;

public enum GlobalErrorMessage {
    // 400
    STORAGE_SIZE_IN_YOUR_WORKSPACE_IS_FULL("Storage size in your workspace is full", 400), //
    ACCOUNT_NOT_FOUND_OR_HAS_BEEN_DELETED("Account not found or has been deleted!", 400), //
    NOT_IN_THE_SAME_WORKSPACE("Not in the same workspace!", 400), //

    // 401
    UNAUTHORIZED("Unauthorized", 401), //
    INVALID_JWT_TOKEN("Invalid JWT token.", 401), //
    JWT_TOKEN_EXPIRED("JWT token is expired", 401), //
    WORKSPACE_IS_INACTIVE_OR_UNPAID("Your workspace is inactive or unpaid", 401), //
    WORKSPACE_HAS_EXPIRED("Your workspace has expired, the campaign function cannot be used", 401), //
    ACCOUNT_IS_LOGGED_IN_ANOTHER_DEVICE("The account is already logged in on another device", 401), //
    REQUIRED_LOGIN("Login to use this function!", 401), //

    // 403
    NOT_HAVE_ACCESS("You do not have Permission to Access this workspace!", 403), //

    // 404
    FILE_NOT_FOUND("File not found", 404);

    public final String message;
    public final int statusCode;

    private GlobalErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
