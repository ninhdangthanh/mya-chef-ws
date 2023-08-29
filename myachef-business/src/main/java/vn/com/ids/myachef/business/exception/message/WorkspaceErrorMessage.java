package vn.com.ids.myachef.business.exception.message;

public enum WorkspaceErrorMessage {

    // 400
    SPECIAL_CHARACTER("There is a special character in your Workspace.", 400), //
    REGISTERED("Registered", 400), //
    CUSTOM_FIELD_IN_YOUR_WORKSPACE_IS_FULL("Custom field in your workspace is full", 400), //
    WORKSPACE_DOES_NOT_EXIST("Workspace does not exist", 400), //

    // 403
    NOT_HAVE_PERMISSION_TO_VIEW("You do not have permission to view workspaces", 403), //
    NOT_HAVE_PERMISSION_TO_CREATE("You do not have permission to create a new workspace", 403), //
    NOT_HAVE_PERMISSION_TO_DELETE("You do not have permission to DELETE a new workspace", 403), //

    // 404
    WORKSPACE_NOT_FOUND("Workspace not found", 404); //

    public final String message;
    public final int statusCode;

    private WorkspaceErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
