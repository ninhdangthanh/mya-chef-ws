package vn.com.ids.myachef.business.service;

import vn.com.ids.myachef.business.payload.reponse.momo.MomoPaymentRedirectBody;
import vn.com.ids.myachef.dao.model.MomoPaymentModel;

public interface MomoPaymentService extends IGenericService<MomoPaymentModel, Long> {

    MomoPaymentModel findByRequestId(String requestId);

    void handleRedirect(MomoPaymentRedirectBody body);

}
