package vn.com.ids.myachef.api.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.SecurityContextService;
import vn.com.ids.myachef.business.converter.CustomerAffiliateDetailConverter;
import vn.com.ids.myachef.business.converter.SaleConverter;
import vn.com.ids.myachef.business.dto.SaleDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.payload.reponse.IntroduceCustomerScopeResponse;
import vn.com.ids.myachef.business.service.CustomerAffiliateDetailService;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.SaleService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.SaleCriteria;
import vn.com.ids.myachef.dao.enums.IntroduceCustomerScope;
import vn.com.ids.myachef.dao.enums.PromotionType;
import vn.com.ids.myachef.dao.enums.SaleType;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.CustomerAffiliateDetailModel;
import vn.com.ids.myachef.dao.model.SaleModel;

@RestController
@RequestMapping("/api/sale")
@Slf4j
@Validated
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleConverter saleConverter;

    @Autowired
    private SecurityContextService securityContextService;

    @Autowired
    private CustomerAffiliateDetailService customerAffiliateDetailService;

    @Autowired
    private CustomerAffiliateDetailConverter customerAffiliateDetailConverter;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "search by criteria")
    @GetMapping("/search")
    public Page<SaleDTO> getByCriteria(@ParameterObject SaleCriteria flashSaleCriteria) {
        log.info("------------------ Search, pagination - START ----------------");
        Page<SaleModel> page = saleService.search(flashSaleCriteria);
        List<SaleDTO> flashSaleDTOs = saleConverter.toBasicDTOs(page.getContent());
        Pageable pageable = PageRequest.of(flashSaleCriteria.getPageIndex(), flashSaleCriteria.getPageSize());
        log.info("------------------ Search, pagination - END ----------------");
        return new PageImpl<>(flashSaleDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Search voucher by code and condition")
    @GetMapping("/code")
    public List<SaleDTO> getByCode(@RequestParam String code, @RequestParam List<String> nhanhVnProductIds, @RequestParam double amount) {
        log.info("------------------ Search voucher by code - START ----------------");
        return saleService.findByCode(code, nhanhVnProductIds, securityContextService.getAuthenticatedUserId(), amount);
    }

    @Operation(summary = "Get all voucher by condition")
    @GetMapping("/voucher")
    public List<SaleDTO> findVoucherByConditon(@RequestParam List<String> nhanhVnProductIds, @RequestParam double amount) {
        log.info("------------------ Get all voucher by condition - START ----------------");
        return saleService.findVoucherByConditon(nhanhVnProductIds, securityContextService.getAuthenticatedUserId(), amount);
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public SaleDTO findById(@PathVariable Long id) {
        log.info("------------------ Find by id - START ----------------");
        return saleService.findById(id, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Find product ids exist in sale by time frame")
    @GetMapping("/product-ids-exist")
    public List<Long> findProductIdsExistByTimeFrame(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate, //
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate, @RequestParam(required = false) Long saleId) {
        log.info("------------------ Find product ids exist in sale by time frame - START ----------------");
        if (saleId == null) {
            if (saleService.existByTimeFrameAndScopeAll(fromDate, toDate)) {
                return productConfigService.findId();
            }

            return saleService.findProductIdsExistByTimeFrame(fromDate, toDate);
        } else {
            if (saleService.existByTimeFrameAndScopeAllIgnoreSaleId(fromDate, toDate, saleId)) {
                return productConfigService.findId();
            }

            return saleService.findProductIdsExistByTimeFrameIgnoreBySaleId(fromDate, toDate, saleId);
        }
    }

    @Operation(summary = "Find by introduceCustomerScope")
    @GetMapping("/introduce-customer-scope")
    public IntroduceCustomerScopeResponse findByIntroduceCustomerScope(@RequestParam IntroduceCustomerScope introduceCustomerScope) {
        log.info("------------------ Find by introduceCustomerScope - START ----------------");
        SaleModel saleModel = saleService
                .findIntroduceCustomerSaleCampaignByScopeIn(Arrays.asList(introduceCustomerScope.name(), IntroduceCustomerScope.ALL.name()));
        if (saleModel == null) {
            throw new ResourceNotFoundException("Not found flash sale with introduceCustomerScope: " + introduceCustomerScope);
        }

        SaleDTO saleDTO = saleConverter.toDTO(saleModel, customerService.isNewCustomer(securityContextService.getAuthenticatedUserId()));
        List<CustomerAffiliateDetailModel> customerAffiliateDetailModels = null;
        if (introduceCustomerScope == IntroduceCustomerScope.REFERRED_CUSTOMER) {
            customerAffiliateDetailModels = customerAffiliateDetailService.findByReferredCustomerId(securityContextService.getAuthenticatedUserId());
        } else if (introduceCustomerScope == IntroduceCustomerScope.AFFILIATE_CUSTOMER) {
            customerAffiliateDetailModels = customerAffiliateDetailService.findByAffiliateCustomerId(securityContextService.getAuthenticatedUserId());
        }
        log.info("------------------ Find by introduceCustomerScope - END ----------------");
        return new IntroduceCustomerScopeResponse(saleDTO, customerAffiliateDetailConverter.toBasicDTOs(customerAffiliateDetailModels));
    }

    @Operation(summary = "Create")
    @PostMapping
    @Validated(OnCreate.class)
    public SaleDTO create(@RequestBody @Valid SaleDTO flashSaleDTO) {
        log.info("------------------ Create - START ----------------");
        return saleService.create(flashSaleDTO, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Update")
    @PutMapping("/{id}")
    public SaleDTO update(@PathVariable Long id, @RequestBody SaleDTO flashSaleDTO) {
        log.info("------------------ Update - START ----------------");
        SaleModel saleModel = saleService.findOne(id);
        if (saleModel == null) {
            throw new ResourceNotFoundException("Not found sale with id: " + id);
        }
        LocalDateTime now = LocalDateTime.now();
        if (saleModel.getStatus() == Status.ACTIVE && (saleModel.getType() == SaleType.FLASH_SALE || saleModel.getType() == SaleType.SALE_CAMPAIGN)
                && now.isAfter(saleModel.getStartDate()) && now.isBefore(saleModel.getEndDate())) {
            throw new BadRequestException("This sale is runing can not update");
        }
        return saleService.update(saleModel, flashSaleDTO, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Stop sale")
    @PatchMapping("/stop/{id}")
    public SaleDTO stopSale(@PathVariable Long id) {
        log.info("------------------ Stop sale - START ----------------");
        SaleModel saleModel = saleService.findOne(id);
        if (saleModel == null) {
            throw new ResourceNotFoundException("Not found sale with id: " + id);
        }

        if (saleModel.getStatus() == Status.ACTIVE
                && (saleModel.getType() == SaleType.FLASH_SALE || saleModel.getType() == SaleType.SALE_CAMPAIGN
                        || saleModel.getType() == SaleType.SALE_FOR_NEW_CUSTOMER)
                && !saleModel.isIntroduceCustomer() && saleModel.getPromotionType() == PromotionType.SALE && saleModel.isRevertPrice()
                && !saleModel.getStartDate().isAfter(LocalDateTime.now())) {
            saleModel.setRevertPrice(false);
            saleService.revertProductPriceBySale(saleModel);
        }
        saleModel.setStatus(Status.IN_ACTIVE);

        saleService.save(saleModel);

        log.info("------------------ Stop sale - END ----------------");

        return saleConverter.toDTO(saleModel, customerService.isNewCustomer(securityContextService.getAuthenticatedUserId()));
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam List<Long> ids) {
        log.info("ids: {}", ids);
        if (!CollectionUtils.isEmpty(ids)) {
            List<SaleModel> saleModels = saleService.findAllById(ids);
            for (SaleModel saleModel : saleModels) {
                saleModel.setStatus(Status.DELETED);
            }
            saleService.saveAll(saleModels);
        }

        log.info("------------------ Delete - END ----------------");
    }
}
