package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.SalePackageDetailType;

@Data
public class SalePackageDetailDTO {

    private Long id;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private SalePackageDetailType type;

    @NotNull(message = "Field 'customerId': {notnull}", groups = { OnCreate.class })
    private Long customerId;

    private Long productId;

    @NotNull(message = "Field 'salePackageId': {notnull}", groups = { OnCreate.class })
    private Long salePackageId;
}
