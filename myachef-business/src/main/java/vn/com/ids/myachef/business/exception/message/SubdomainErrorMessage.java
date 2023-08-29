package vn.com.ids.myachef.business.exception.message;

public enum SubdomainErrorMessage {

    // 400
    SUBDOMAIN_ALREADY_EXISTS_OR_ERROR("Subdomain already exists or other error!", 400);

    public final String message;
    public final int statusCode;

    private SubdomainErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
