package vn.com.ids.myachef.business.exception.message;

public enum RoleGroupErorrMessage {

    // 404
    ROLEGROUP_NOT_FOUND("Not found Role Group with id: %s", 404);

    public final String message;
    public final int statusCode;

    private RoleGroupErorrMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
