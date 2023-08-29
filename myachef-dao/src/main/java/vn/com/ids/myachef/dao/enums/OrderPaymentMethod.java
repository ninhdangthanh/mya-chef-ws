package vn.com.ids.myachef.dao.enums;

public enum OrderPaymentMethod {
    COD("COD"), GATEWAY("Gateway");

    private final String value;

    OrderPaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
