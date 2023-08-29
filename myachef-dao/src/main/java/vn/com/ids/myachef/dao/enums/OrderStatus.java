package vn.com.ids.myachef.dao.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum OrderStatus {
    NEW("New"), // đơn mới
    CONFIRMING("Confirming"), CUSTOMER_CONFIRMING("CustomerConfirming"), CONFIRMED("Confirmed"), // đang xác nhận, chờ khách xác nhận, đã xác nhận
    PACKING("Packing"), PACKED("Packed"), // đang đống gói, đã đống gói
    CHANGE_DEPOT("ChangeDepot"), // đổi kho xuất hàng
    PICKUP("Pickup"), SHIPPING("Shipping"), // chờ thu gom, đang chuyển
    SUCCESS("Success"), FAILED("Failed"), // thành công, thất bại
    CANCELED("Canceled"), ABORTED("Aborted"), CARRIER_CANCELED("CarrierCanceled"), // Khách hủy, Hệ thống hủy, hãng vận chuyển hủy
    SOLD_OUT("SoldOut"), // hết hàng
    RETURNING("Returning"), RETURNED("Returned"); // đang chuyển hoàn, đã chuyển hoàn

    private final String value;

    private static final Map<String, OrderStatus> ENUM_STATUS_MAP;

    static {
        Map<String, OrderStatus> map = new ConcurrentHashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            map.put(status.getValue(), status);
        }
        ENUM_STATUS_MAP = Collections.unmodifiableMap(map);
    }

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatus getByValue(String value) {
        return ENUM_STATUS_MAP.get(value);
    }
}
