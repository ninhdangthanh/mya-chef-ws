package vn.com.ids.myachef.business.exception.message;

public enum MarketingCampaignErrorMessage {

    // 400
    MARKETING_CAMPAIGN_IN_YOUR_WORKSPACE_IS_FULL("Marketing campaign in your workspace is full", 400), //
    MARKETING_CAMPAIGN_PROCESSING_IN_YOUR_WORKSPACE_IS_FULL("Marketing campaign processing in your workspace is full", 400), //

    FIELDS_CONDITIONFIELDNAME_NOT_NULL_WHEN_DELAYTYPEDITION("Fields ConditionFieldName can not null when DelayTypedition", 400), //
    FIELDS_CONDITIONTIMETYPE_OR_CONDITION_OR_CONDITIONFIELDNAME_EQUALS_CONCONDITIONTIME_NOT_NULL_WHEN_DELAYTYPEDITION(
            "Fields ConditionTimeType or Condition  or ConditionFieldName or  = conConditionTime can not null when DelayTypedition", 400), //

    FIELD_WAITINGDATE_NOT_NULL("Field waitingDate is not null", 400), //
    FIELD_STEPWAITINGTIME_OR_WAITINGTIMETYPE_NOT_NULL("Field stepWaitingTime or waitingTimeType is not null", 400), //

    END_DATE_AFTER_START_DATE("End date must be after start date", 400), //
    FIELD_TOCUSTOMERGROUPID_NOT_NULL_WHEN_ACTION_EQUALS_MOVE("Field toCustomerGroupId can not null when action = move", 400), //

    FIELD_CUSTOMERID_NOT_NULL("Field customerId can not be null", 400), //
    FIELD_ZALOACCOUNTID_NOT_NULL("Field zaloAccountId can not be null", 400), //

    FIELD_FIRSTNAME_OR_CUSTOMERGENDER_OR_CUSTOMERPHONENUMBER_OR_LASTNAME_NOT_NULL_WHEN_ISMYSELF_EQUALS_FALSE(
            "Field firstName or customerGender or customerPhoneNumber or lastName can not be null when isMySelf = false", 400), //

    // 404
    MARKETING_CAMPAIGN_NOT_FOUND("Not found Marketing campaign with id: %s", 404);

    public final String message;
    public final int statusCode;

    private MarketingCampaignErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
