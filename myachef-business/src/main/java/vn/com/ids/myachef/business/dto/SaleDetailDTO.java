package vn.com.ids.myachef.business.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;

@Getter
@Setter
public class SaleDetailDTO {

    private Long id;

    private Double discount;

    private Double discountPercent;

    private Double maxPromotion;

    private Long totalQuantity;

    private Long totalQuantityUsed;

    private Long maxQuantityUseInUser;

    @NotNull(message = "Field 'productId': {notnull}", groups = { OnCreate.class })
    private Long productId;

    private ProductConfigDTO productDTO;
}
