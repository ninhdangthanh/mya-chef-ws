package vn.com.ids.myachef.dao.enums;

public enum OrderType {
    
    SHIPPING("Shipping"), //
    SHOPPING("Shopping"), //
    PREORDER("PreOrder");

    private final String value;

    OrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
