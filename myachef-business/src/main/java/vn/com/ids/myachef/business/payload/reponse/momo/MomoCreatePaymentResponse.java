package vn.com.ids.myachef.business.payload.reponse.momo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoCreatePaymentResponse {

    private String partnerCode;

    private String orderId;

    private String requestId;

    private Double amount;

    private String responseTime;

    private String message;

    private int resultCode;

    private String payUrl;

    private String deeplink;

    private String qrCodeUrl;
}
