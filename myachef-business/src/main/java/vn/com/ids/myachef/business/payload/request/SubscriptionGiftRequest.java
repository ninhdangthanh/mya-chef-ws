package vn.com.ids.myachef.business.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionGiftRequest {

    private Long giftId;

    private Integer quantity;

    private String nhanhVnProductId;
}
