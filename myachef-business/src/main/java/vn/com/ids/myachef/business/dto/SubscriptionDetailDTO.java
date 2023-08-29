package vn.com.ids.myachef.business.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.SubscriptionDetailType;

@Getter
@Setter
public class SubscriptionDetailDTO {

    private Long id;

    private SubscriptionDetailType type;

    private String link;

    private String nhanhVnProductCategoryId;

    private Double giftMoney;

    private String nhanhVnProductId;

    private String description;

    private Integer quantity;

    private Long coin;

    private Integer limit;

    private Double minimumOrderPrice;

    private List<Long> conditionSubscriptionIds;
}
