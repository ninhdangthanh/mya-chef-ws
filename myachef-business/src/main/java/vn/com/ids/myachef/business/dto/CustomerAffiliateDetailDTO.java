package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;

@Getter
@Setter
public class CustomerAffiliateDetailDTO {
    private Long id;

    @NotNull(message = "Field 'affiliateCustomerId': {notnull}", groups = { OnCreate.class })
    private Long affiliateCustomerId;

    @NotNull(message = "Field 'referredCustomerId': {notnull}", groups = { OnCreate.class })
    private Long referredCustomerId;

    private String affiliateCustomerFullName;

    private String affiliateCustomerAvatar;

    private String referredCustomerFullName;

    private String referredCustomerAvatar;

    private LocalDateTime createdDate;
}
