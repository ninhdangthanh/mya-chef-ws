package vn.com.ids.myachef.business.exception.message;

public enum MomoErrorMessage {

    // 400
    REFERENCEID_ALREADY_EXISTS("ReferenceId already exists", 400);

    public final String message;
    public final int statusCode;

    private MomoErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
