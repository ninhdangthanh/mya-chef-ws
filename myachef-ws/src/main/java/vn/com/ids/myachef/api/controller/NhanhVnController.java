package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.NhanhVnConverter;
import vn.com.ids.myachef.business.dto.NhanhVnDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.NhanhVnAPIService;
import vn.com.ids.myachef.business.service.NhanhVnService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.model.NhanhVnModel;

@RestController
@RequestMapping("/api/nhanh-vn")
@Slf4j
@Validated
public class NhanhVnController {

    @Autowired
    private NhanhVnAPIService nhanhVnAPIService;

    @Autowired
    private NhanhVnService nhanhVnService;

    @Autowired
    private NhanhVnConverter nhanhVnConverter;

    @Operation(summary = "Get access token")
    @GetMapping("/access-token")
    public NhanhVnDTO getAccessToken() {
        log.info("------------------ Get access token - START ----------------");
        NhanhVnModel nhanhVnModel = nhanhVnService.getAccessToken();
        log.info("------------------ Get access token - END ----------------");
        return nhanhVnConverter.toBasicDTO(nhanhVnModel);
    }

    @Operation(summary = "nhanh vn redirect URL")
    @GetMapping("/redirect-url")
    public void nhanhVnRedirectURL(@RequestParam String accessCode) {
        log.info("------------------ nhanh vn redirect URL - START ----------------");
        nhanhVnAPIService.getAccessToken(accessCode);
        log.info("------------------ nhanh vn redirect URL - END ----------------");
    }

    @Operation(summary = "Find all nhanh vn account")
    @GetMapping("/all")
    public List<NhanhVnDTO> findAll() {
        log.info("------------------ Find all nhanh vn account - START ----------------");
        List<NhanhVnModel> nhanhVnModels = nhanhVnService.findAll();
        log.info("------------------ Find all nhanh vn account - END ----------------");
        return nhanhVnConverter.toBasicDTOs(nhanhVnModels);
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public NhanhVnDTO findById(@PathVariable Long id) {
        log.info("------------------ Find by id - START ----------------");
        NhanhVnModel nhanhVnModel = nhanhVnService.findOne(id);
        if (nhanhVnModel == null) {
            throw new ResourceNotFoundException("Not found nhanh vn account with id: " + id);
        }
        log.info("------------------ Find by id - END ----------------");
        return nhanhVnConverter.toBasicDTO(nhanhVnModel);
    }

    @Operation(summary = "Create")
    @PostMapping
    @Validated(OnCreate.class)
    public NhanhVnDTO create(@RequestBody @Valid NhanhVnDTO nhanhVnDTO) {
        log.info("------------------ Create - START ----------------");
        NhanhVnModel nhanhVnModel = nhanhVnService.create(nhanhVnDTO);
        log.info("------------------ Create - END ----------------");
        return nhanhVnConverter.toBasicDTO(nhanhVnModel);
    }

    @Operation(summary = "Nhanh vn web hooks callback url")
    @PostMapping("/web-hooks")
    public ResponseEntity<?> receiveWebHooks(@RequestBody String data) {
        log.info("------------------ Nhanh vn web hooks callback url - START ----------------");
        return nhanhVnAPIService.receiveWebHooks(data);
    }

    @Operation(summary = "Update")
    @PatchMapping("/{id}")
    public NhanhVnDTO update(@RequestBody NhanhVnDTO nhanhVnDTO, @PathVariable Long id) {
        log.info("------------------ Update - START ---------------");
        NhanhVnModel nhanhVnModel = nhanhVnService.findOne(id);
        if (nhanhVnModel == null) {
            throw new ResourceNotFoundException("Not found nhanh vn account with id: " + id);
        }
        nhanhVnService.update(nhanhVnModel, nhanhVnDTO);
        log.info("------------------ Update - END ----------------");
        return nhanhVnConverter.toBasicDTO(nhanhVnModel);
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam List<Long> ids) {
        log.info("ids: {}", ids);
        if (!CollectionUtils.isEmpty(ids)) {
            nhanhVnService.deleteByIds(ids);
        }

        log.info("------------------ Delete - END ----------------");
    }
}
