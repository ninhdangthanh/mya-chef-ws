package vn.com.ids.myachef.business.exception.message;

public enum NoteErrorMessage {

    // 400
    START_DATE_BEFORE_END_DATE("Start date must be before end date", 400), //

    // 404
    NOTE_NOT_FOUND("Not found Note with id: %s", 404);

    public final String message;
    public final int statusCode;

    private NoteErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
