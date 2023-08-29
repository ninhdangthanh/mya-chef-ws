package vn.com.ids.myachef.business.exception.message;

public enum EmployeeDeviceErrorMessage {

    // 404
    EMPLOYEE_DEVICE_NOT_FOUND("Not found Employee Device with id: %s", 404);

    public final String message;
    public final int statusCode;

    private EmployeeDeviceErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
