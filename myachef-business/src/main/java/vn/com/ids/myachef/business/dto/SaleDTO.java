package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.IntroduceCustomerScope;
import vn.com.ids.myachef.dao.enums.PromotionType;
import vn.com.ids.myachef.dao.enums.SaleScope;
import vn.com.ids.myachef.dao.enums.SaleType;
import vn.com.ids.myachef.dao.enums.Status;

@Data
public class SaleDTO {

    private Long id;

    @NotBlank(message = "Field 'name': {notblank}", groups = { OnCreate.class })
    private String name;

    @NotBlank(message = "Field 'code': {notblank}", groups = { OnCreate.class })
    private String code;

    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Status status;

    @NotNull(message = "Field 'startDate': {notnull}", groups = { OnCreate.class })
    private LocalDateTime startDate;

    @NotNull(message = "Field 'endDate': {notnull}", groups = { OnCreate.class })
    private LocalDateTime endDate;

    @NotNull(message = "Field 'type': {notnull}", groups = { OnCreate.class })
    private SaleType type;

    @NotNull(message = "Field 'saleScope': {notnull}", groups = { OnCreate.class })
    private SaleScope saleScope;

    private PromotionType promotionType;

    private Double minimumOrderPrice;

    private Long totalQuantity;

    private Long maxQuantityUseInUser;

    private Long totalQuantityUsed;

    private Long maxPromotion;

    private Double discount;

    private Double discountPercent;

    private Double receiveMoneyPercent;

    private Double receiveMammyCoin;

    private int totalQuantityProduct;

    private boolean canUse;

    private boolean isIntroduceCustomer = false;

    private IntroduceCustomerScope introduceCustomerScope;

    private List<SaleDetailDTO> saleDetailDTOs = new ArrayList<>();
}
