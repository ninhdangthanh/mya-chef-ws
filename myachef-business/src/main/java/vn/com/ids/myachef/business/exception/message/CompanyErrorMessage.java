package vn.com.ids.myachef.business.exception.message;

public enum CompanyErrorMessage {

    // 400
    VAT_ALREADY_IN_USE("Your VAT is already in use", 400), //
    NOT_HAVE_COMPANY_IN_EXPORT_LIST("Not have Company in Export list", 400), //
    HOTLINE_IS_INVALID("Hotline is invalid", 400), //

    // 404
    COMPANY_NOT_FOUND("Not found Company with id: %s", 404);

    public final String message;
    public final int statusCode;

    private CompanyErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
