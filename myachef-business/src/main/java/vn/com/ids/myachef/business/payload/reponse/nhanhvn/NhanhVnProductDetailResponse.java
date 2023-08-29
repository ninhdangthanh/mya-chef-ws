package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnProductDetailResponse {

    private String idNhanh;

    private String privateId;

    private String parentId;

    private String merchantCategoryId;

    private String merchantProductId;

    private String categoryId;

    private String brandId;

    private String brandName;

    private String code;

    private String barcode;

    private String name;

    private String otherName;

    private Double importPrice;

    private Double oldPrice;

    private Double price;

    private String wholesalePrice;

    private String image;

    private String unit;

    private List<String> images;

    private String status;

    private String previewLink;

    private String advantages;

    private String description;

    private String content;

    private int showHot;

    private int showNew;

    private int showHome;

    private Long shippingWeight;

    private Long width;

    private Long length;

    private Long height;

    private String vat;

    private String createdDateTime;

    private NhanhVnInventoryResponse inventory;

    private String warranty;

    private String warrantyAddress;

    private String warrantyPhone;

    private List<String> videos;
}
