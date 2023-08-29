package vn.com.ids.myachef.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.SystemConfigConverter;
import vn.com.ids.myachef.business.dto.SystemConfigDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.SystemConfigService;
import vn.com.ids.myachef.dao.model.SystemConfigModel;

@RestController
@RequestMapping("/api/system-config")
@Slf4j
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemConfigConverter systemConfigConverter;

    @Operation(summary = "Find one")
    @GetMapping()
    public SystemConfigDTO findOne() {
        log.info("------------------ Find one - START ----------------");
        List<SystemConfigModel> systemConfigModels = systemConfigService.findAll();
        SystemConfigModel systemConfigModel = null;
        if (!CollectionUtils.isEmpty(systemConfigModels)) {
            systemConfigModel = systemConfigModels.get(0);
        }
        if (systemConfigModel == null) {
            throw new ResourceNotFoundException("Not found systemConfig");
        }
        log.info("------------------ Find one - END ----------------");
        return systemConfigConverter.toBasicDTO(systemConfigModel);
    }

    @Operation(summary = "Create or update")
    @PatchMapping
    public SystemConfigDTO createOrUpdate(@RequestBody SystemConfigDTO systemConfigDTO) {
        log.info("------------------ Create or update - START ----------------");
        return systemConfigService.createOrUpdate(systemConfigDTO);
    }
}
