package vn.com.ids.myachef.business.exception.message;

public enum MarketingCampaignStatisticalErrorMessage {

    // 400
    FIELD_MARKETINGCAMPAIGNID_NOT_NULL("Field marketingCampaignId can not null", 400), //
    FIELD_INTERACTION_NOT_NULL("Field interaction can not null", 400), //
    FIELD_CUSTOMER_ID_NOT_NULL("Field customerId can not null", 400);

    public final String message;
    public final int statusCode;

    private MarketingCampaignStatisticalErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
