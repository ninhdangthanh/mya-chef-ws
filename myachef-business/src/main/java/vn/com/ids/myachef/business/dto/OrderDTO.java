package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.ids.myachef.business.payload.request.SubscriptionGiftRequest;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.OrderPaymentMethod;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.enums.OrderType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    private String nhanhVnId;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private OrderStatus status;

    private String note;

    private OrderType type;

    @NotNull(message = "Field `paymentMethod` can be not null", groups = OnCreate.class)
    private OrderPaymentMethod paymentMethod;
    private Double totalMoney;

    private Double totalAmount; // Tổng tiền hàng
    private Double shipFee;

    private Double totalMoneyDiscount; // Voucher giảm giá
    private Double totalMammnyCoinReceive;
    private Double shipDiscount;

    private Double totalShipFee;

    private Long coin;

    private CustomerDTO customerDTO;

    private String recipientName;

    private String recipientPhoneNumber;

    private String recipientChildName;

    private String recipientChildGender;

    private String recipientCity;

    private String recipientDistrict;

    private String recipientWard;

    private String recipientAddress;

    private String paymentImage;
    
    private Double totalPayment;

    private List<ProductConfigDTO> productDTOs = new ArrayList<>();

    private List<Long> cartIds;

    private String nhanhVnProductId;

    private int quantity;

    private List<Long> voucherIds = new ArrayList<>();

    private List<SubscriptionGiftRequest> subscriptionGifts = new ArrayList<>();
}
