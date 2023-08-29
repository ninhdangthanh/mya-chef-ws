package vn.com.ids.myachef.business.exception.message;

public enum MediaStorageErrorMessage {

    // 400
    CAN_NOT_CREATE_THUMBNAIL("Can not create thumbnail from your video", 400),//
    
    // 404
    MEDIASTORAGE_NOT_FOUND("Not found MediaStorage with id: %s", 404);

    public final String message;
    public final int statusCode;

    private MediaStorageErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
