package vn.com.ids.myachef.business.exception.message;

public enum ForgotPasswordErrorMessage {
    
    //400
    OTPCODE_INVALID_OR_EXPIRED("OtpCode is invalid or expired!", 400), //
    DISABLED_ACCOUNT("The account has been disabled please contact admin", 400);

    public final String message;
    public final int statusCode;

    private ForgotPasswordErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
