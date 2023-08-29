package vn.com.ids.myachef.business.exception.message;

public enum SubscriptionPlanErrorMessage {

    // 400
    SUBSCRIPTION_PLAN_CONFIG_NOT_NULL("Subscription Plan Config DTO can not be null", 400), //
    START_DATE_BEFORE_END_DATE("Start date must be before end date", 400), //
    INVALID_PRICE("Invalid price", 400), // discountPercent
    INVALID_DISCOUNTPERCENT("Invalid Discount Percent", 400), //
    INVALID_DISCOUNT("Invalid Discount", 400), //
    CAN_NOT_DISCOUNT_THE_FREE_PLAN("Can not discount the free plan", 400),

    // 404
    SUBSCRIPTION_PLAN_NOT_FOUND("Not found Subscription Plan with id: %s", 404);

    public final String message;
    public final int statusCode;

    private SubscriptionPlanErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
