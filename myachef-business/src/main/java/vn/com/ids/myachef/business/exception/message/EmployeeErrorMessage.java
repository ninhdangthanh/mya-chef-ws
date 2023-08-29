package vn.com.ids.myachef.business.exception.message;

public enum EmployeeErrorMessage {

    // 400
    USERNAME_USED("Username used", 400), //
    EMPLOYEE_IN_YOUR_WORKSPACE_IS_FULL("Employee in your workspace is full", 400), //
    THE_ACCOUNT_HAS_BEEN_DISABLED("The account has been disabled please contact admin", 400), //
    NOT_HAVE_EMPLOYEE_IN_EXPORT_LIST("Not have Employee in Export list", 400), //
    CANNOT_SEND_EMAIL("Cannot send email", 400), //
    OTP_EXPIRED("otp expired", 400), //
    DISABLED_ACCOUNT("disabled account", 400), //
    INVALID_IDENTITYCARD("Invalid IdentityCard", 400), //
    ROLE_ID_CAN_NOT_BE_NULL("Role Id can not be null", 400), //
    EMAIL_IS_INVALID("Field 'email': is invalid", 400), //
    PHONE_NUMBER_IS_INVALID("Field 'phoneNumber': is invalid", 400), //
    WORKSPACE_DOES_NOT_EXIST("Workspace does not exist", 400), //
    PASSWORD_DOES_NOT_MATCH("Password does not match", 400), //
    OLD_PASSWORD_AND_NEW_PASSWORD_CANNOT_BE_SAME("Old Password and New Password cannot be same", 400), //

    // 403
    NOT_HAVE_PERMISSON("You do not have permission to use this function", 403), //
    // 404
    EMPLOYEE_NOT_FOUND("Not found Employee with id: %s", 404), //
    EMPLOYEE_NOT_FOUND_BY_USERNAME_WORKSPACE("Not found Employee with username: %s and workspace", 404), //
    EMPLOYEE_NOT_FOUND_BY_TOKEN("Not found Employee with Token: %s", 404), //
    EMPLOYEE_NOT_FOUND_BY_OTP("Not found Employee with OTP: %s", 404), //

    ASSIGNEE_NOT_FOUND("Not found Assignee with id: %s", 404), //

    CREATEBY_NOT_FOUND("Not found createby with id: %s", 404), //
    UPDATEBY_NOT_FOUND("Not found updateby with id: %s", 404); //

    public final String message;
    public final int statusCode;

    private EmployeeErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
