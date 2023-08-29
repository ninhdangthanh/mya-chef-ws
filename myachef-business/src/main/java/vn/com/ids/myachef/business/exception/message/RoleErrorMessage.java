package vn.com.ids.myachef.business.exception.message;

public enum RoleErrorMessage {

    // 400
    HAS_MEMBERS_CAN_NOT_BE_DELETED("The role has members so it can't be deleted", 400), //

    // init from backend
    NAME_ALREADY_EXISTS("name (key) already exists", 400), //
    FIELD_NODE_PARENT_NAME_NOTBLANK("Field node parent name not blank", 400), //
    FIELD_NODE_CHILD_NAME_NOTBLANK("Field node child name not blank", 400), //
    FIELD_NODE_GRANDCHILD_NAME_NOTBLANK("Field node Grandchild name not blank", 400), //

    // 404
    DEFAULT_PERMISSION_NOT_FOUND("Default permission not found", 404), //
    ROLE_NOT_FOUND("Not found Role with id: %s", 404);

    public final String message;
    public final int statusCode;

    private RoleErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
