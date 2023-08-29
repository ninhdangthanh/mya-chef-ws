package vn.com.ids.myachef.business.exception.message;

public enum SubscriptionErrorMessage {

    // 400
    INVALID_WORKSPACE("Invalid workspace", 400), //
    CONTRACT_ID_ALREADY_EXIST("Contract Id already exist", 400), //
    WORKSPACE_ALREADY_EXIST("Workspace already exist", 400), //
    ROLE_GROUP_OR_ROLE_DOSE_NOT_EXIST("Role Group or Role does not exists", 400), //
    SUBSCRIPTION_ALREADY_EXIST("Subscription already exists", 400), //
    USERBILLING_HAS_PHONE_NUMBER_ALREADY_EXIST("User billing has phone number already exist", 400), //
    EMPLOYEE_HAVE_PHONE_NUMBER_AND_WORKSPACE_ALREADY_EXIST("Employee have phone number and workspace already exist", 400), //
    UNSUPPORTED_OBJECT("Unsupported object", 400), //
    SUBSCRIPTION_BONUS_UPDATE_TIME_OUT("Subscription bonus update time out", 400), //

    // 9/12/2022
    BOTH_PAYMENTSTATUS_AND_PERIOD_MUST_TRIAL_OR_ANOTHER("Both payment status and period must trial or another", 400), //

    // 403
    NOT_HAVE_PERMISSION_CREATE("You do not have permission create subscription", 403), //

    // 404
    SUBSCRIPTION_NOT_FOUND("Not found Subscription with id: %s", 404), //
    SUBSCRIPTION_NOT_FOUND_BY_WORKSPACE("Not found Subscription by worksapce", 404), //
    SUBSCRIPTION_HISTORY_NOT_FOUND("Not found Subscription history with id: %s", 404);

    public final String message;
    public final int statusCode;

    private SubscriptionErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
