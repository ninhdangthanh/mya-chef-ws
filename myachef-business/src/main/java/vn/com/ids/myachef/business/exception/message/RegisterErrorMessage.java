package vn.com.ids.myachef.business.exception.message;

public enum RegisterErrorMessage {
    // 400
    ACCOUNT_ALREADY_EXISTS("Account already exists", 400), //
    WAIT_FOR_CONTACT("Account has workspace, please wait for contact from sales staff", 400), //
    USERBILLING_ALREADY_EXISTS("User billing already exists", 400), //
    ROLE_GROUP_OR_ROLE_NOT_EXITS("Role Group or Role does not exist", 400), //
    UNABLE_SEND_OTP("Unable to send OTP", 400), //
    ACCOUNT_NOT_FOUND_OR_BLOCK("Account not found or block", 400), //
    INVALID_WORKSPACE("Invalid workspace", 400), //
    INVALID_TOKEN("Invalid token!", 400), //
    USERNAME_IS_NULL("Username is null", 400), //
    WORKSPACE_DOES_NOT_EXIST("Workspace does not exist", 400), //
    SUBSCRIPTIONPLAN_NOT_EXISTS("SubscriptionPlan does not exists", 400), //
    WORKSPACE_ALREADY_EXIST("Workspace already exist", 400), //

    OTP_CODE_INVALID_OR_EXPIRED("OTP Code is invalid or expired", 400), //

    // 403
    NOT_HAVE_PERMISSION_TO_CREATE("You do not have permission to create a new workspace", 403);

    public final String message;
    public final int statusCode;

    private RegisterErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
