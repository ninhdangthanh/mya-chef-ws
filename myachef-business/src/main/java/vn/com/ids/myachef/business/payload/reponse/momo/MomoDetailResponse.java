package vn.com.ids.myachef.business.payload.reponse.momo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MomoDetailResponse {

    private String partnerCode;

    private String orderId;

    private String requestId;

    private String extraData;

    private Double amount;

    private String transId;

    private String payType;

    private int resultCode;

    private List<String> refundTrans;

    private String message;

    private String responseTime;

    private String lastUpdated;
}
