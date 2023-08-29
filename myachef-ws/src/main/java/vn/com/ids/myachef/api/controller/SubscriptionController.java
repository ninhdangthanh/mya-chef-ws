package vn.com.ids.myachef.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.SecurityContextService;
import vn.com.ids.myachef.business.converter.SubscriptionConverter;
import vn.com.ids.myachef.business.converter.SubscriptionCustomerDetailConverter;
import vn.com.ids.myachef.business.dto.SubscriptionCustomerDetailDTO;
import vn.com.ids.myachef.business.dto.SubscriptionDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.SubscriptionCustomerDetailService;
import vn.com.ids.myachef.business.service.SubscriptionService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.SubscriptionCriteria;
import vn.com.ids.myachef.dao.model.SubscriptionModel;

@RestController
@RequestMapping("/api/subscription")
@Slf4j
@Validated
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionConverter subscriptionConverter;

    @Autowired
    private SecurityContextService securityContextService;

    @Autowired
    private SubscriptionCustomerDetailService subscriptionCustomerDetailService;

    @Autowired
    private SubscriptionCustomerDetailConverter subscriptionCustomerDetailConverter;

    @Autowired
    private HttpServletRequest request;

    @Operation(summary = "search by criteria")
    @GetMapping("/search")
    public Page<SubscriptionDTO> getByCriteria(@ParameterObject SubscriptionCriteria criteria) {
        log.info("------------------ Search, pagination - START ----------------");
        Page<SubscriptionModel> page = subscriptionService.search(criteria);
        List<SubscriptionDTO> subscriptionDTOs = subscriptionConverter.toBasicDTOs(page.getContent(), request);
        Pageable pageable = PageRequest.of(criteria.getPageIndex(), criteria.getPageSize());
        log.info("------------------ Search, pagination - END ----------------");
        return new PageImpl<>(subscriptionDTOs, pageable, page.getTotalElements());
    }

    @GetMapping("/find-all")
    public List<SubscriptionDTO> findAll() {
        log.info("------------------ Find all - START ----------------");
        List<SubscriptionModel> subscriptionModels = subscriptionService.findAll();
        log.info("------------------ Find all - END ----------------");
        return subscriptionConverter.toBasicDTOs(subscriptionModels, request);
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public SubscriptionDTO findById(@PathVariable Long id) {
        log.info("------------------ Find by id - START ----------------");
        SubscriptionModel subscriptionModel = subscriptionService.findOne(id);
        if (subscriptionModel == null) {
            throw new ResourceNotFoundException("Not found subscription with id: " + id);
        }
        log.info("------------------ Find by id - END ----------------");
        return subscriptionConverter.toDTO(subscriptionModel, null, request);
    }

    @Operation(summary = "Find by id for customer")
    @GetMapping("/customer/{id}")
    public SubscriptionDTO findByIdForCustomer(@PathVariable Long id) {
        log.info("------------------ Find by id for customer - START ----------------");
        SubscriptionModel subscriptionModel = subscriptionService.findOne(id);
        if (subscriptionModel == null) {
            throw new ResourceNotFoundException("Not found subscription with id: " + id);
        }
        log.info("------------------ Find by id for customer - END ----------------");
        return subscriptionConverter.toDTOForCustomer(subscriptionModel, request);
    }

    @Operation(summary = "Find paid subscription")
    @GetMapping("/paid")
    public List<SubscriptionDTO> findPaid() {
        log.info("------------------ Find paid subscription - START ----------------");
        return subscriptionService.findPaid(securityContextService.getAuthenticatedUserId(), request);
    }

    @Operation(summary = "Find your gift")
    @GetMapping("/gift")
    public List<SubscriptionCustomerDetailDTO> findYourGift() {
        log.info("------------------ Find your gift - START ----------------");
        return subscriptionCustomerDetailService.findYourGift(securityContextService.getAuthenticatedUserId(), request);
    }

    @Operation(summary = "Get all gift by condition")
    @GetMapping("/gift-can-use")
    public List<SubscriptionCustomerDetailDTO> findGiftCanUse(@RequestParam Double totalAmount, @RequestParam(required = false) List<Long> cartIds,
            @RequestParam(required = false) String nhanhVnProductId, @RequestParam(required = false, defaultValue = "0") int quantity) {
        log.info("------------------ Get all gift by condition - START ----------------");
        if (CollectionUtils.isEmpty(cartIds) && (nhanhVnProductId == null && quantity <= 0)) {
            throw new BadRequestException("Field cartIds or (nhanhVnProductId and quantity) is required");
        }
        return subscriptionCustomerDetailConverter.toDTOAndAddSubscriptionDetails(
                subscriptionCustomerDetailService.findGiftCanUse(totalAmount, 2l, cartIds, nhanhVnProductId, quantity), request);
    }

    @Operation(summary = "Create ")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SubscriptionDTO create(@RequestPart SubscriptionDTO subscriptionDTO, @RequestParam(required = false) MultipartFile image) {
        log.info("------------------ Create - START ----------------");
        return subscriptionService.create(subscriptionDTO, image, request);
    }

    @Operation(summary = "Buy subcription")
    @PostMapping("/buy")
    @Validated(OnCreate.class)
    public SubscriptionCustomerDetailDTO buy(@RequestParam Long subscriptionId,
            @RequestBody @Valid SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO) {
        log.info("------------------ Buy subcription - START ----------------");
        return subscriptionService.buy(subscriptionId, securityContextService.getAuthenticatedUserId(), subscriptionCustomerDetailDTO, request);
    }

    @Operation(summary = "Update")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SubscriptionDTO update(@PathVariable Long id, @RequestPart SubscriptionDTO subscriptionDTO, @RequestParam(required = false) MultipartFile image) {
        log.info("------------------ Update - START ----------------");
        SubscriptionModel subscriptionModel = subscriptionService.findOne(id);
        if (subscriptionModel == null) {
            throw new ResourceNotFoundException("Not found subscription with id: " + id);
        }
        return subscriptionService.update(subscriptionModel, subscriptionDTO, image, request);
    }

    // @Operation(summary = "Delete")
    // @DeleteMapping
    // public void delete(@RequestParam List<Long> ids) {
    // log.info("ids: {}", ids);
    // if (!CollectionUtils.isEmpty(ids)) {
    // subscriptionService.deleteByIds(ids);
    // }
    //
    // log.info("------------------ Delete - END ----------------");
    // }
}
