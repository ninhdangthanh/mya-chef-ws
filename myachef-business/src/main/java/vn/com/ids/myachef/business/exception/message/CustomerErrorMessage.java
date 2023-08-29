package vn.com.ids.myachef.business.exception.message;

public enum CustomerErrorMessage {

    // 400
    CUSTOMER_IN_YOUR_WORKSPACE_IS_FULL("Customer in your workspace is full", 400), //
    PHONE_NUMBER_ALREADY_EXIST("Phone number already exist", 400), //
    EMAIL_IS_INVALID("Field 'email': is invalid", 400), //
    PHONE_NUMBER_IS_INVALID("Field 'phoneNumber': is invalid", 400), //

    NOT_HAVE_CUSTOMER_IN_EXPORT_LIST("Not have Customer in Export list", 400), //
    DO_NOT_SUPPORT_SORT_BY_FIELD_NAME("Do not support sort by field name: %s", 400), //
    LAST_NAME_OR_FIRST_NAME_OR_PHONE_NUMBER_IS_REQUIRED("Cell last_name first_name phone_number is required", 400), //

    CUSTOM_FIELD_NAME_NOT_CONTAIN_IN_WORKSPACE("Custom field name %s not contain in workspace", 400), //

    CELL_LAST_NAME_AND_CELL_FIRST_NAME_OR_CELL_FULL_NAME_IS_REQUIRED("Cell Last_name and cell First_name or cell Full_name is required", 400), //
    CELL_PHONE_NUMBER_IS_REQUIRED("Cell Full_name is required", 400), //

    // 404
    CUSTOMER_NOT_FOUND("Not found Customer with id: %s", 404), //
    CUSTOMER_UPLOAD_NOT_FOUND("Not found Customer Upload with id: %s", 404), //
    CUSTOMER_EDITED_FILE_NOT_FOUND("Customer edited file not found", 404);

    public final String message;
    public final int statusCode;

    private CustomerErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}