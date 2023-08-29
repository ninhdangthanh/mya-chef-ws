package vn.com.ids.myachef.business.payload.request;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculateRequest {
    private List<Long> cartIds = new ArrayList<>();

    private List<Long> voucherIds = new ArrayList<>();

    private String nhanhVnProductId;
    private int quantity;

    private List<SubscriptionGiftRequest> subscriptionGifts = new ArrayList<>();

    private Long coin;
}
