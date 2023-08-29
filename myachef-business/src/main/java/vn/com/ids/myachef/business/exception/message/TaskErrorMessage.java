package vn.com.ids.myachef.business.exception.message;

public enum TaskErrorMessage {
    // 400
    NOT_HAVE_TASK_IN_EXPORT_LIST("Not have Task in Export list", 400), //

    NOT_HAVE_TASKHISTORY_IN_EXPORT_LIST("Not have TaskHistory in Export list", 400), //

    // 404
    TASK_UPLOAD_NOT_FOUND("Not found Task upload with id: %s", 404),//
    TASK_NOT_FOUND("Not found Task with id: %s", 404);

    public final String message;
    public final int statusCode;

    private TaskErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
