package vn.com.ids.myachef.business.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnUpdate;
import vn.com.ids.myachef.dao.enums.CustomerStatus;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDTO {

    @NotNull(message = "Field `id` can not be null", groups = OnUpdate.class)
    private Long id;

    private String nhanhVnId;

    private String username;

    private String phoneNumber;

    private String fullName;

    private CustomerStatus status;

    private String avatar;

    private LocalDate birthday;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String city;
    private Integer cityId;

    private String district;
    private Integer districtId;

    private String ward;
    private Integer wardId;

    private String address;

    private String referrerCode;

    private String affiliateCode;

    private String childName;

    private Boolean childGender;

    private LocalDate childBirthday;

    private Boolean isFollowOA = false;

    private Long totalCustomerInPutReferralCode;

    private Boolean isNewCustomer;

    private Long coin;
}
