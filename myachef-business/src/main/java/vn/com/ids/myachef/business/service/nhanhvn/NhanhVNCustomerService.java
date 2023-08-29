package vn.com.ids.myachef.business.service.nhanhvn;

import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVNCustomerRequest;
import vn.com.ids.myachef.business.service.nhanhvn.response.NhanhVNCustomerResponse;

public interface NhanhVNCustomerService {

    public NhanhVNCustomerResponse createOrUpdate(NhanhVNCustomerRequest customerRequest);

}
