package vn.com.ids.myachef.business.exception.message;

public enum MarketingCampaignCustomerErrorMessage {

    // 404
    MARKETING_CAMPAIGN_CUSTOMER_NOT_FOUND("Not found Marketing campaign customer with id: %s", 404), //
    PHONENUMBER_NOT_CONTAIN_ANY_CONTACTS("The phone number does not contain any contacts", 404);

    public final String message;
    public final int statusCode;

    private MarketingCampaignCustomerErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
