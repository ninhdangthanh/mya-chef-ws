package vn.com.ids.myachef.business.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.ids.myachef.business.dto.SystemConfigDTO;
import vn.com.ids.myachef.dao.model.SystemConfigModel;

@Component
public class SystemConfigConverter {

    @Autowired
    private ModelMapper mapper;

    public SystemConfigDTO toBasicDTO(SystemConfigModel systemConfigModel) {
        return mapper.map(systemConfigModel, SystemConfigDTO.class);
    }

    public SystemConfigModel toModel(SystemConfigDTO systemConfigDTO) {
        return mapper.map(systemConfigDTO, SystemConfigModel.class);
    }

    public void update(SystemConfigModel systemConfigModel, SystemConfigDTO systemConfigDTO) {
        if (systemConfigDTO.getMinimunFreeShipOrderTotalPrice() != null) {
            systemConfigModel.setMinimunFreeShipOrderTotalPrice(systemConfigDTO.getMinimunFreeShipOrderTotalPrice());
        }

        if (systemConfigDTO.getShipPrice() != null) {
            systemConfigModel.setShipPrice(systemConfigDTO.getShipPrice());
        }
    }
}
