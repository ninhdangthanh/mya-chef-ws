package vn.com.ids.myachef.business.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.OnlinePaymentType;
import vn.com.ids.myachef.dao.enums.PaymentStatus;
import vn.com.ids.myachef.dao.enums.SubscriptionCustomerType;

@Getter
@Setter
public class SubscriptionCustomerDetailDTO {

    private Long id;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private PaymentStatus paymentStatus;

    private OnlinePaymentType paymentType;

    private SubscriptionCustomerType type;

    private LocalDate expiredDate;

    private String billImage;

    @NotBlank(message = "Field 'customerFullName': {notblank}", groups = { OnCreate.class })
    private String customerFullName;

    @NotBlank(message = "Field 'customerOrderPhoneNumber': {notblank}", groups = { OnCreate.class })
    private String customerOrderPhoneNumber;

    private int giftQuantity;

    private int giftTotalUsed;

    private String giftDescription;

    private boolean canUseGift;

    private Integer limit;

    private SubscriptionDTO subscriptionDTO;

    private CustomerDTO customerDTO;

    private SubscriptionDetailDTO subscriptionDetailDTO;
}
