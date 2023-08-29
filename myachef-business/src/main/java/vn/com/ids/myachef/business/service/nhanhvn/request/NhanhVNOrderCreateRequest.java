package vn.com.ids.myachef.business.service.nhanhvn.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.enums.OrderType;

@Getter
@Setter
@NoArgsConstructor
public class NhanhVNOrderCreateRequest {

    private String id;
    private String customerName;
    private String customerMobile;
    private String customerAddress;
    private String customerCityName;
    private String customerDistrictName;
    private String customerWardLocationName;
    private int customerShipFee;
    private int moneyTransfer;
    private int carrierId;
    private String status = OrderStatus.NEW.getValue();
    private String type = OrderType.SHIPPING.getValue();
    private String paymentMethod;
    private String description;
    private double moneyDiscount;

    private List<Product> productList = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    public class Product {
        private int id;
        private int idNhanh;
        private int quantity;
        private String name;
        private int price;
        private String type = "Product";
    }
}
