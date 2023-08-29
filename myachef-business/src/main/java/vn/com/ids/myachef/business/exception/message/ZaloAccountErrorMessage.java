package vn.com.ids.myachef.business.exception.message;

public enum ZaloAccountErrorMessage {
    // 400
    PHONE_NUMBER_IS_ALREADY_IN_THE_WORKSPACE("Your Phone number is already in the workspace", 400), //
    PHONE_NUMBER_IS_ALREADY_IN_ANOTHER_WORKSPACE("Your phone number is already in another workspace", 400), //
    ZALO_ACCOUNT_IS_ACTIVE_IN_YOUR_WORKSPACE_IS_FULL("Zalo account is active in your workspace is full", 400), //

    // 403
    NOT_HAVE_PERMISSION_TO_UPDATE("You do not have permission to update status", 403), //
    NOT_HAVE_PERMISSION_TO_CREATE_DEFAULT_ADMIN_ACCOUNT("You do not have permission to create default admin account", 403),

    // 404
    DEFAULT_ZALO_ACCOUNT_NOT_FOUND("Not found default zalo account", 404), //
    ZALO_ACCOUNT_NOT_FOUND("Not found Zalo Account with id: %s", 404), //
    NOT_FOUND_DESKTOP_ADMIN_APP_CONNECTED("Not found desktop admin app connected", 404), //
    NOT_FOUND_DESKTOP_CLIENT_APP_CONNECTED("Not found desktop client app connected", 404);

    public final String message;
    public final int statusCode;

    private ZaloAccountErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
