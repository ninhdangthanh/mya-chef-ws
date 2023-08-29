package vn.com.ids.myachef.dao.enums;

public enum MomoPaymentConfirmType {
    CAPTURE("capture")//
    , REVERT_AUTHORIZE("revertAuthorize");

    private final String typeLabel;

    private MomoPaymentConfirmType(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public final String getTypeLabel() {
        return typeLabel;
    }

}
