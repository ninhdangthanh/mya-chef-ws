package vn.com.ids.myachef.business.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.business.validation.group.OnUpdate;

@Getter
@Setter
public class SubscriptionDTO {

    private Long id;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @NotBlank(message = "Field 'name': {notblank}", groups = { OnCreate.class, OnUpdate.class })
    private String name;

    @NotNull(message = "Field 'month': {notnull}", groups = { OnCreate.class, OnUpdate.class })
    private Integer month;

    private Double moneyRefund;

    private Double price;

    private String tagLine;

    private Boolean freeShip;

    private String image;

    private boolean isUpdateDetails;

    private LocalDate expiredDate;

    private String purchaseUrl;

    private List<SubscriptionDetailDTO> subscriptionDetailDTOs = new ArrayList<>();
}
