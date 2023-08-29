package vn.com.ids.myachef.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import vn.com.ids.myachef.api.security.SecurityContextService;
import vn.com.ids.myachef.business.converter.CustomerConverter;
import vn.com.ids.myachef.business.dto.CustomerAffiliateDetailDTO;
import vn.com.ids.myachef.business.dto.CustomerDTO;
import vn.com.ids.myachef.business.dto.SubscriptionDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.payload.request.ProfileRequest;
import vn.com.ids.myachef.business.service.CustomerAffiliateDetailService;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.SubscriptionService;
import vn.com.ids.myachef.business.validation.group.OnUpdate;
import vn.com.ids.myachef.dao.criteria.CustomerAffiliateDetailCriteria;
import vn.com.ids.myachef.dao.criteria.CustomerCriteria;
import vn.com.ids.myachef.dao.model.CustomerModel;

@RestController
@RequestMapping("/api/customers")
@Slf4j
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private SecurityContextService securityContextService;

    @Autowired
    private CustomerAffiliateDetailService customerAffiliateDetailService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private HttpServletRequest request;

    @Operation(summary = "Find by criteria")
    @GetMapping("/search")
    public Page<CustomerDTO> findBycriteria(@ParameterObject CustomerCriteria customerCriteria) {
        log.info("------------------ CustomerController - Find by criteria - START ----------------");
        Page<CustomerModel> page = customerService.findAll(customerCriteria);
        List<CustomerDTO> customerDTOs = customerConverter.toDTOs(page.getContent());
        Pageable pageable = PageRequest.of(customerCriteria.getPageIndex(), customerCriteria.getPageSize());
        log.info("------------------ CustomerController - Find by criteria - END ----------------");
        return new PageImpl<>(customerDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public CustomerDTO findbyId(@PathVariable("id") Long id) {
        log.info("------------------ CustomerController - findById - START ----------------");
        CustomerModel customerModel = customerService.findOne(id);
        if (customerModel == null) {
            throw new ResourceNotFoundException(String.format("Not found Customer by id: %s", id));
        }
        log.info("------------------ CustomerController - findById - END ----------------");
        return customerConverter.toDTO(customerModel);
    }

    @Operation(summary = "Find Info")
    @GetMapping("/info")
    public CustomerDTO findInfo() {
        log.info("------------------ CustomerController - findInfo - START ----------------");
        CustomerModel customerModel = customerService.findOne(securityContextService.getAuthenticatedUserId());
        if (customerModel == null) {
            throw new ResourceNotFoundException(String.format("Not found Customer by id: %s", securityContextService.getAuthenticatedUserId()));
        }
        log.info("------------------ CustomerController - findInfo - END ----------------");
        CustomerDTO customerDTO = customerConverter.toDTO(customerModel);
        customerDTO.setIsNewCustomer(customerService.isNewCustomer(securityContextService.getAuthenticatedUserId()));
        return customerDTO;
    }

    @Operation(summary = "Get total your order status")
    @GetMapping("/total-your-order")
    public Map<String, Integer> findTotalYourOrderStatus() {
        log.info("------------------ Get total your order status - START ----------------");
        return customerService.findTotalYourOrderStatus(securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Search customer affiliate criteria")
    @GetMapping("/search-affiliate")
    public Page<CustomerAffiliateDetailDTO> search(@ParameterObject CustomerAffiliateDetailCriteria customerAffiliateDetailCriteria) {
        log.info("------------------ Search customer affiliate criteria - START ----------------");
        return customerAffiliateDetailService.search(customerAffiliateDetailCriteria);
    }

    @Operation(summary = "Find your subscription")
    @GetMapping("/your-subscription")
    public List<SubscriptionDTO> findYourSubscription() {
        log.info("------------------ Find your subscription - START ----------------");
        return subscriptionService.findYourSubscription(securityContextService.getAuthenticatedUserId(), request);
    }

    @Operation(summary = "Get referred customer by time")
    @GetMapping("/referred-customer")
    public Page<CustomerDTO> findReferredCustomerByPeriodTime(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, //
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate, //
            @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "10") int pageSize) {
        log.info("------------------ Get referred customer by time - START ----------------");
        return customerAffiliateDetailService.findReferredCustomerByPeriodTime(fromDate, toDate, pageIndex, pageSize);
    }

    @Operation(summary = "Find customer by ReferredCustomerId")
    @GetMapping("/affiliate-customer/{referredCustomerId}")
    public List<CustomerDTO> FindCustomerByReferredCustomerId(@PathVariable Long referredCustomerId) {
        log.info("------------------ Find customer by ReferredCustomerId - START ----------------");
        return customerService.FindCustomerByReferredCustomerId(referredCustomerId);
    }

    @Operation(summary = "Update by Zalo Profile")
    @PatchMapping("/info/zalo-profile")
    public CustomerDTO updateByZaloProfile(@RequestBody ProfileRequest profileRequest) {
        log.info("------------------ CustomerController - Update - START ----------------");
        return customerService.updateByZaloProfile(profileRequest, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Update")
    @PatchMapping
    @Validated(OnUpdate.class)
    public CustomerDTO update(@Valid @RequestBody CustomerDTO customerDTO) {
        log.info("------------------ CustomerController - Update - START ----------------");
        CustomerDTO customerResponse = customerService.update(customerDTO);
        log.info("------------------ CustomerController - Update - END ----------------");
        return customerResponse;
    }

    @Operation(summary = "Use affiliate code")
    @PatchMapping("/use-affiliate/{affiliateCode}")
    public void useAffiliateCode(@PathVariable String affiliateCode) {
        log.info("------------------ Use affiliate code - START ----------------");
        customerService.useAffiliateCode(affiliateCode, securityContextService.getAuthenticatedUserId());
        log.info("------------------ Use affiliate code - END ----------------");
    }

    //
    // @Operation(summary = "Delete")
    // @DeleteMapping
    // public void delete(@RequestParam List<Long> ids) {
    // log.info("------------------ CustomerController - Delete - START ----------------");
    // if (!CollectionUtils.isEmpty(ids)) {
    // customerService.deleteByIds(ids);
    // }
    // log.info("------------------ CustomerController - Delete - END ----------------");
    // }

}
