package vn.com.ids.myachef.business.service;

import vn.com.ids.myachef.business.payload.reponse.momo.MomoDetailResponse;
import vn.com.ids.myachef.dao.enums.PaymentSource;
import vn.com.ids.myachef.dao.model.MomoPaymentModel;

public interface MomoAPIService {

    MomoPaymentModel createPaymentRequest(Long id, PaymentSource paymentSource, Long price, String returnUrl);

    MomoDetailResponse getPaymentStatus(String requestId);

    void refund(String requestId, String transId, Long price, String description);
}
