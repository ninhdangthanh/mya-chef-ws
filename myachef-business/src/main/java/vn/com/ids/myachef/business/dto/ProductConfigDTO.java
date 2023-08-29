package vn.com.ids.myachef.business.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class ProductConfigDTO {

    private Long id;

    @NotBlank(message = "Field 'name': {notblank}", groups = { OnCreate.class })
    private String name;

    @NotNull(message = "Field 'price': {notnull}", groups = { OnCreate.class })
    private Double price;

    private Double oldPrice;

    private Double importPrice;

    private String image;

    private List<String> images;

    @NotBlank(message = "Field 'nhanhVnId': {notblank}", groups = { OnCreate.class })
    private String nhanhVnId;

    private String nhanhVnCategoryId;

    private Status status;

    private boolean isNewProduct = false;

    private boolean existInDatabase;

    private int orderQuantity = 0;

    private double orderTotalPrice = 0;

    private String description;

    private String freeShipDescription;

    private List<ProductConfigDTO> relatedProducts;

    private int homeProductConfigOrder;

    private long saleQuantity;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ProductConfigDTO other = (ProductConfigDTO) obj;
        return id == other.id;
    }

}
