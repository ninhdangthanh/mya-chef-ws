package vn.com.ids.myachef.business.exception.message;

public enum EmailConfigErrorMessage {
    // 400
    USERNAME_ALREADY_EXISTS("Username in workspace already exists!", 400), //
    NEED_ONE_DEFAULT_EMAIL("Workspace needs at least 1 default email", 400), //

    PASSWORD_NOT_BLANK("Password not blank", 400),//
    AUTHENTICATION_FAILURES("Authentication failures", 400),//
    OTHER_FAILURES("Other failures", 400),//
    
    
    // 404
    EMAILCONFIG_NOT_FOUND("Not found EmailConfig with id: %s", 404), //
    EMAILCONFIG_NOT_FOUND_BY_WORKSPACE("Not found EmailConfig with workspace", 404);

    public final String message;
    public final int statusCode;

    private EmailConfigErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
