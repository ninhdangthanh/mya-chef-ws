package vn.com.ids.myachef.business.service.nhanhvn;

import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVnLocationRequest;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNLocationResponse;

public interface NhanhVNShippingService {

    public NhanhVNLocationResponse getLocation(NhanhVnLocationRequest nhanhVnLocationRequest);

}
