package vn.com.ids.myachef.business.exception.message;

public enum AuthenticationErrorMessage {
    // 400
    USERNAME_IS_INVALID("Username is invalid!", 400), //
    WRONG_DEVICE_KEY("Wrong device key", 400), //
    INVALID_WORKSPACE("Workspace is invalid!", 400), //
    WORKSPACE_IS_NOT_FOUND_OR_UNABLE_TO_START("Workspace is NOT FOUND or UNABLE TO START!", 400), //
    EMPLOYEE_NOT_FOUND_OR_PASSWORD_IS_NOT_MATCH("Employee not found or password is not match", 400), //
    REFRESH_TOKEN_ON_BLACKLIST("Refresh token is on Blacklist!", 400), //

    // 401
    WORKSPACE_HAS_NOT_ARRIVED_OR_HAS_EXPIRED("Your workspace has not arrived or has expired", 401);//

    public final String message;
    public final int statusCode;

    private AuthenticationErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
