package vn.com.ids.myachef.business.exception.message;

public enum AutomationSigninZaloErrorMessage {
    AUTOMATION_SIGNIN_ZALO_NOT_FOUND("Not found automation signin zalo with id: %s", 404);

    public final String message;
    public final int statusCode;

    private AutomationSigninZaloErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
