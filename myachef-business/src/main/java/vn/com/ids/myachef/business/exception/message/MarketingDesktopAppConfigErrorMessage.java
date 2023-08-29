package vn.com.ids.myachef.business.exception.message;

public enum MarketingDesktopAppConfigErrorMessage {

    // 403
    NOT_HAVE_PERMISSION_TO_VIEW("You do not have permission to view", 403), //
    
    // 404
    MARKETING_DESKTOP_APP_CONFIG_NOT_FOUND("Not found Marketing Desktop App Config", 404);

    public final String message;
    public final int statusCode;

    private MarketingDesktopAppConfigErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
