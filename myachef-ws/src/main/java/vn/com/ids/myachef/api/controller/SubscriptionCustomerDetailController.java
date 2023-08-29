package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.SubscriptionCustomerDetailConverter;
import vn.com.ids.myachef.business.dto.SubscriptionCustomerDetailDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.SubscriptionCustomerDetailService;
import vn.com.ids.myachef.dao.criteria.SubscriptionCustomerDetailCriteria;
import vn.com.ids.myachef.dao.enums.SubscriptionCustomerType;
import vn.com.ids.myachef.dao.model.SubscriptionCustomerDetailModel;

@RestController
@RequestMapping("/api/subscription-gift")
@Slf4j
@Validated
public class SubscriptionCustomerDetailController {

    @Autowired
    private SubscriptionCustomerDetailService subscriptionCustomerDetailService;

    @Autowired
    private SubscriptionCustomerDetailConverter subscriptionCustomerDetailConverter;

    @Autowired
    private HttpServletRequest request;

    @Operation(summary = "Search and pagination customer buy subscriptions")
    @GetMapping("/search-buy")
    public Page<SubscriptionCustomerDetailDTO> searchCustomerBuySubscriptions(@ParameterObject SubscriptionCustomerDetailCriteria criteria) {
        log.info("------------------ searchCustomerBuySubscriptions - START ----------------");
        criteria.setType(SubscriptionCustomerType.BUY);
        Page<SubscriptionCustomerDetailModel> page = subscriptionCustomerDetailService.search(criteria);
        List<SubscriptionCustomerDetailDTO> subscriptionCustomerDetailDTOs = null;
        if (criteria.getIsGetFullObjectData() != null && criteria.getIsGetFullObjectData().booleanValue()) {
            subscriptionCustomerDetailDTOs = subscriptionCustomerDetailConverter.toDTOs(page.getContent(), request);
        } else {
            subscriptionCustomerDetailDTOs = subscriptionCustomerDetailConverter.toBasicDTOs(page.getContent(), request);
        }
        Pageable pageable = PageRequest.of(criteria.getPageIndex(), criteria.getPageSize());
        log.info("------------------ searchCustomerBuySubscriptions - END ----------------");
        return new PageImpl<>(subscriptionCustomerDetailDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public SubscriptionCustomerDetailDTO findById(@PathVariable Long id) {
        log.info("------------------ Find by id - START ----------------");
        SubscriptionCustomerDetailModel subscriptionCustomerDetailModel = subscriptionCustomerDetailService.findOne(id);
        if (subscriptionCustomerDetailModel == null) {
            throw new ResourceNotFoundException("Not found subscription customer detail with id: " + id);
        }
        log.info("------------------ Find by id - END ----------------");
        return subscriptionCustomerDetailConverter.toDTO(subscriptionCustomerDetailModel, request);
    }

    @Operation(summary = "Upload bill")
    @PatchMapping(value = "/upload-bill/{subscriptionCustomerDetailId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SubscriptionCustomerDetailDTO uploadBill(@PathVariable Long subscriptionCustomerDetailId, @RequestParam String billUrl) {
        log.info("------------------ Upload bill - START ----------------");
        return subscriptionCustomerDetailService.uploadBill(subscriptionCustomerDetailId, billUrl, request);
    }

    @Operation(summary = "Update subscription customer detail status")
    @PatchMapping(value = "/subscription-customer-detail/{subscriptionCustomerDetailId}")
    public SubscriptionCustomerDetailDTO updateSubscriptionCustomerDetailStatus(@PathVariable Long subscriptionCustomerDetailId,
            @RequestBody SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO) {
        log.info("------------------ Update subscription customer detail - START ----------------");
        return subscriptionCustomerDetailService.updateStatus(subscriptionCustomerDetailId, subscriptionCustomerDetailDTO, request);
    }
}
