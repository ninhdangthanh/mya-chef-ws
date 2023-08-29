package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
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
import vn.com.ids.myachef.business.converter.StoreInformationConverter;
import vn.com.ids.myachef.business.dto.StoreInformationDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.StoreInformationService;
import vn.com.ids.myachef.dao.criteria.StoreInformationCriteria;
import vn.com.ids.myachef.dao.model.StoreInformationModel;

@RestController
@RequestMapping("/api/store-informations")
@Slf4j
public class StoreInformationController {

    @Autowired
    private StoreInformationService storeInformationService;

    @Autowired
    private StoreInformationConverter storeInformationConverter;

    @Operation(summary = "Find by criteria")
    @GetMapping("/search")
    public Page<StoreInformationDTO> findBycriteria(@ParameterObject StoreInformationCriteria storeInformationCriteria) {
        log.info("------------------ StoreInformationController - Find by criteria - START ----------------");
        Page<StoreInformationModel> page = storeInformationService.findAll(storeInformationCriteria);
        List<StoreInformationDTO> storeInformationDTOs = storeInformationConverter.toDTOs(page.getContent());
        Pageable pageable = PageRequest.of(storeInformationCriteria.getPageIndex(), storeInformationCriteria.getPageSize());
        log.info("------------------ StoreInformationController - Find by criteria - END ----------------");
        return new PageImpl<>(storeInformationDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public StoreInformationDTO findbyId(@PathVariable("id") Long id) {
        log.info("------------------ StoreInformationController - findById - START ----------------");
        StoreInformationModel storeInformationModel = storeInformationService.findOne(id);
        if (storeInformationModel == null) {
            throw new ResourceNotFoundException("Not found `StoreInfomation` by id: " + id);
        }

        log.info("------------------ StoreInformationController - findById - END ----------------");
        return storeInformationConverter.toDTO(storeInformationModel);
    }

    @Operation(summary = "Find by default")
    @GetMapping("/default")
    public StoreInformationDTO findbyIsDefaultTrue() {
        log.info("------------------ StoreInformationController - findById - START ----------------");
        StoreInformationModel storeInformationModel = storeInformationService.findByIsDefaultTrue();
        if (storeInformationModel == null) {
            throw new ResourceNotFoundException("Not found default StoreInfomation");
        }

        log.info("------------------ StoreInformationController - findById - END ----------------");
        return storeInformationConverter.toDTO(storeInformationModel);
    }

    @Operation(summary = "Create")
    @PostMapping
    public StoreInformationDTO create(@Valid @RequestBody StoreInformationDTO storeInformationRequest) {
        log.info("------------------ StoreInformationController - Create - START ----------------");
        StoreInformationDTO storeInformationResponse = storeInformationService.create(storeInformationRequest);
        log.info("------------------ StoreInformationController - Create - END ----------------");
        return storeInformationResponse;
    }

    @Operation(summary = "Update")
    @PatchMapping
    public StoreInformationDTO update(@RequestBody StoreInformationDTO storeInformationRequest) {
        log.info("------------------ StoreInformationController - Update - START ----------------");
        StoreInformationDTO storeInformationResponse = storeInformationService.update(storeInformationRequest);
        log.info("------------------ StoreInformationController - Update - END ----------------");
        return storeInformationResponse;
    }

    @Operation(summary = "Delete")
    @DeleteMapping()
    public void delete(@RequestParam List<Long> ids) {
        log.info("------------------ StoreInformationController - Delete - START ----------------");
        if (!CollectionUtils.isEmpty(ids)) {
            storeInformationService.deleteByIds(ids);
        }
        log.info("------------------ StoreInformationController - Delete - END ----------------");
    }

}
