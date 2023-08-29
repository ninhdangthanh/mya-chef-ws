package vn.com.ids.myachef.business.service;

import vn.com.ids.myachef.business.dto.NhanhVnDTO;
import vn.com.ids.myachef.dao.model.NhanhVnModel;

public interface NhanhVnService extends IGenericService<NhanhVnModel, Long> {

    NhanhVnModel findByBiggestPriority();

    NhanhVnModel create(NhanhVnDTO nhanhVnDTO);

    void update(NhanhVnModel nhanhVnModel, NhanhVnDTO nhanhVnDTO);

    NhanhVnModel getAccessToken();

}
