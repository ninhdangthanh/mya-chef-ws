package vn.com.ids.myachef.business.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.SystemConfigConverter;
import vn.com.ids.myachef.business.dto.SystemConfigDTO;
import vn.com.ids.myachef.business.service.SystemConfigService;
import vn.com.ids.myachef.dao.model.SystemConfigModel;
import vn.com.ids.myachef.dao.repository.SystemConfigRepository;

@Service
@Transactional
@Slf4j
public class SystemConfigServiceImpl extends AbstractService<SystemConfigModel, Long> implements SystemConfigService {

    private SystemConfigRepository systemConfigRepository;

    protected SystemConfigServiceImpl(SystemConfigRepository systemConfigRepository) {
        super(systemConfigRepository);
        this.systemConfigRepository = systemConfigRepository;
    }

    @Autowired
    private SystemConfigConverter systemConfigConverter;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Override
    public SystemConfigDTO createOrUpdate(SystemConfigDTO systemConfigDTO) {
        List<SystemConfigModel> systemConfigModels = systemConfigRepository.findAll();
        SystemConfigModel systemConfigModel = null;
        if (!CollectionUtils.isEmpty(systemConfigModels)) {
            // Update
            systemConfigModel = systemConfigModels.get(0);

            systemConfigConverter.update(systemConfigModel, systemConfigDTO);
        } else {
            // Create
            systemConfigModel = systemConfigConverter.toModel(systemConfigDTO);
        }

        systemConfigRepository.save(systemConfigModel);

        log.info("------------------ Create or update - END ----------------");
        return systemConfigConverter.toBasicDTO(systemConfigModel);
    }

    @Override
    public String getFreeShipDescription() {
        List<SystemConfigModel> systemConfigModels = systemConfigRepository.findAll();
        String price = applicationConfig.getDefaultMinimunPriceForFreeShip().toString();
        if (!CollectionUtils.isEmpty(systemConfigModels)) {
            SystemConfigModel systemConfigModel = systemConfigModels.get(0);
            if (systemConfigModel.getMinimunFreeShipOrderTotalPrice() != null) {
                price = systemConfigModel.getMinimunFreeShipOrderTotalPrice().toString();
            }
        }
        return "Free ship cho đơn từ " + price + "k";
    }
}
