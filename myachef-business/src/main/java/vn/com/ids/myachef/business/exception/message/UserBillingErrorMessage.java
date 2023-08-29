package vn.com.ids.myachef.business.exception.message;

public enum UserBillingErrorMessage {

    // 400
    PHONE_NUMBER_ALREADY_IN_USE("phone number is already in use", 400),//
    
    // 404
    USERBILLING_NOT_FOUND("Not found UserBilling with id: %s", 404);
    
    public final String message;
    public final int statusCode;

    private UserBillingErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
