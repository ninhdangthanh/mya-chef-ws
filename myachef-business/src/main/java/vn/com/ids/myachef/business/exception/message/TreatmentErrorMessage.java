package vn.com.ids.myachef.business.exception.message;

public enum TreatmentErrorMessage {

    // 404
    TREATMENT_NOT_FOUND("Not found Treatment with id: %s", 404),//
    TREATMENT_COURSE_NOT_FOUND("Not found Treatment course with id: %s", 404);

    public final String message;
    public final int statusCode;

    private TreatmentErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
