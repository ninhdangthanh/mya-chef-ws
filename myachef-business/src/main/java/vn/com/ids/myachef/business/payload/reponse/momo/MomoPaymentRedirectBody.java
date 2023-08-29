package vn.com.ids.myachef.business.payload.reponse.momo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoPaymentRedirectBody {

    private String partnerCode;

    private String orderId;

    private String requestId;

    private Long amount;

    private String orderInfo;

    private String orderType;

    private String transId;

    private int resultCode;

    private String message;

    private String payType;

    private String responseTime;

    private String extraData;

    private String signature;
}
