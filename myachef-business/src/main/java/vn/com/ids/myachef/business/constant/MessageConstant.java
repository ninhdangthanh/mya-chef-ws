package vn.com.ids.myachef.business.constant;

public class MessageConstant {

    private MessageConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CREATED_SUCCESS_MESSAGE = "%s has been successfully created!";
    public static final String UPDATED_SUCCESS_MESSAGE = "%s has been successfully saved!";
    public static final String DELETE_SUCCESS_MESSAGE = "%s has been successfully removed!";

    public static final String CREATED_FAILED_MESSAGE = "%s creation failed!";
    public static final String UPDATED_FAILED_MESSAGE = "%s updation failed!";
    public static final String DELETE_FAILED_MESSAGE = "%s deletion failed!";
    public static final String INCORRECT_EXCEL_FILE_FORMAT_MESSAGE = "Incorrect excel file format";
    public static final String WORKSPACE_DOES_NOT_EXIST = "Workspace does not exist";
}
