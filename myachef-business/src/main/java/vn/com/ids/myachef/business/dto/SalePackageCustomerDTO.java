package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.business.validation.group.OnUpdate;
import vn.com.ids.myachef.dao.enums.SalePackagePeriod;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class SalePackageCustomerDTO {
    private Long id;

    @NotBlank(message = "Field 'name': {notblank}", groups = { OnCreate.class, OnUpdate.class })
    private String name;

    private Integer periodTime;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @NotNull(message = "Field 'period': {notnull}", groups = { OnCreate.class, OnUpdate.class })
    private SalePackagePeriod period;

    private Status status;

    private Set<Long> productIds;

    private List<ProductConfigDTO> receivedProducts;
    
    private List<ProductConfigDTO> notReceivedProducts;
}
