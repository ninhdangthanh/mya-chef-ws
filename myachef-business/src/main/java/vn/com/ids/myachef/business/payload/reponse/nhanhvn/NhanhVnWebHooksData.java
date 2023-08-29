package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnWebHooksData {

    private String productId;

    private String shopProductId;

    private String categoryId;

    private Long brandId;

    private String parentId;

    private String code;

    private String name;

    private Double price;

    private Double vat;

    private String image;

    private String images;

    private String status;

    private String description;

    private String content;

    private int length;

    private int width;

    private int height;

    private String createdDateTime;

    private String attributes;

    private NhanhVnWebHooksInventory inventories;
}
