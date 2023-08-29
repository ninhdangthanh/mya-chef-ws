package vn.com.ids.myachef.business.exception.message;

public enum DesktopAppStatusErrorMessage {

    // 400
    FIELD_ZALOACCOUNT_ID_ZALOACCOUNT_PHONENUMBER_NOT_NULL("Field zaloAccountId and zaloAccountPhoneNumber can not be null when type is client", 400), //
    DESKTOPID_NOT_NULL("Field desktopId can not be null when type is admin", 400);//

    public final String message;
    public final int statusCode;

    private DesktopAppStatusErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
