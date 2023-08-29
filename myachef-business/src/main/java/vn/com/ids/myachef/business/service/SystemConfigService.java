package vn.com.ids.myachef.business.service;

import vn.com.ids.myachef.business.dto.SystemConfigDTO;
import vn.com.ids.myachef.dao.model.SystemConfigModel;

public interface SystemConfigService extends IGenericService<SystemConfigModel, Long> {

    SystemConfigDTO createOrUpdate(SystemConfigDTO systemConfigDTO);

    String getFreeShipDescription();

}
