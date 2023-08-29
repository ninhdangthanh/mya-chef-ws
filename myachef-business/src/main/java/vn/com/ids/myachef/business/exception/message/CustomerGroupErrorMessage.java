package vn.com.ids.myachef.business.exception.message;

public enum CustomerGroupErrorMessage {

    // 400
    CUSTOMER_GROUP_IN_YOUR_WORKSPACE_IS_FULL("customer group in your workspace is full", 400), //
    POTENTIAL_CUSTOMER_GROUP_IN_YOUR_WORKSPACE_IS_FULL("potential customer group in your workspace is full", 400), //

    // 404
    CUSTOMERGROUP_NOT_FOUND("Not found CustomerGroup with id: %s", 404);

    public final String message;
    public final int statusCode;

    private CustomerGroupErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
