package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnProductDataDetailResponse {
    private String idNhanh;
    private String privateId;
    private String parentId;
    private String brandId;
    private String brandName;
    private String typeId;
    private String typeName;
    private String avgCost;
    private String merchantCategoryId;
    private String merchantProductId;
    private String categoryId;
    private String code;
    private String barcode;
    private String name;
    private String otherName;
    private String importPrice;
    private String oldPrice;
    private String price;
    private String wholesalePrice;
    private String image;
    private String status;
    private int showHot;
    private int showNew;
    private int showHome;
    private String order;
    private String previewLink;
    private String shippingWeight;
    private String width;
    private String length;
    private String height;
    private String vat;
    private String createdDateTime;
    // private Inventory inventory;
    private String warrantyAddress;
    private String warrantyPhone;
    private String warranty;
    private String countryName;
    private String unit;
    // private Map<String, ComboProduct> combos;

    // @Getter
    // @Setter
    // public class Inventory {
    // private int remain;
    // private int shipping;
    // private int damaged;
    // private int holding;
    // private int warranty;
    // private int warrantyHolding;
    // private int available;
    // private List<String> depots;
    // }
    //
    //
    // @Getter
    // @Setter
    // public class ComboProduct {
    // private String quantity;
    // private String code;
    // private String name;
    // }

}
