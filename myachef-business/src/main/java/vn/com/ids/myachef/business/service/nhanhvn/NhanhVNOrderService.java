package vn.com.ids.myachef.business.service.nhanhvn;

import vn.com.ids.myachef.business.payload.reponse.order.CalculateResponse;
import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVNOrderUpdateRequest;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNOrderCreateResponse;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNOrderUpdateResponse;
import vn.com.ids.myachef.dao.model.OrderModel;

public interface NhanhVNOrderService {

    public NhanhVNOrderCreateResponse create(OrderModel orderModel, CalculateResponse calculateResponse);

    public NhanhVNOrderUpdateResponse update(NhanhVNOrderUpdateRequest nhanhVNOrderUpdateRequest);

}
