package vn.com.ids.myachef.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.CustomerConverter;
import vn.com.ids.myachef.business.dto.CustomerDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.payload.request.ProfileRequest;
import vn.com.ids.myachef.business.service.CustomerAffiliateDetailService;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.GiftService;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.business.service.SaleService;
import vn.com.ids.myachef.business.service.ZaloSocialService;
import vn.com.ids.myachef.business.service.nhanhvn.NhanhVNCustomerService;
import vn.com.ids.myachef.business.service.nhanhvn.request.NhanhVNCustomerRequest;
import vn.com.ids.myachef.business.zalo.social.UserProfile;
import vn.com.ids.myachef.business.zalo.social.ZaloUser;
import vn.com.ids.myachef.dao.criteria.CustomerCriteria;
import vn.com.ids.myachef.dao.criteria.builder.CustomerSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.CustomerStatus;
import vn.com.ids.myachef.dao.enums.GiftType;
import vn.com.ids.myachef.dao.enums.IntroduceCustomerScope;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.model.CustomerAffiliateDetailModel;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.model.GiftModel;
import vn.com.ids.myachef.dao.model.SaleModel;
import vn.com.ids.myachef.dao.repository.CustomerRepository;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl extends AbstractService<CustomerModel, Long> implements CustomerService {

    @Autowired
    private ZaloSocialService zaloSocialService;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private NhanhVNCustomerService nhanhVNCustomerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private GiftService giftService;

    private CustomerRepository customerRepository;

    protected CustomerServiceImpl(CustomerRepository customerRepository) {
        super(customerRepository);
        this.customerRepository = customerRepository;
    }

    @Autowired
    private CustomerAffiliateDetailService customerAffiliateDetailService;

    public Specification<CustomerModel> buildSpecification(CustomerCriteria customerCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new CustomerSpecificationBuilder(root, criteriaBuilder) //
                .setReferrerAffiliateCode(customerCriteria.getReferrerAffiliateCode())//
                .setSearchText(customerCriteria.getSearchText())//
                .setStatus(customerCriteria.getStatus())//
                .build();
    }

    @Override
    public Page<CustomerModel> findAll(CustomerCriteria customerCriteria) {
        Specification<CustomerModel> specification = buildSpecification(customerCriteria);
        Pageable pageable = buildPageable(customerCriteria);
        return customerRepository.findAll(specification, pageable);
    }

    @Override
    public CustomerModel findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public CustomerModel register(ZaloUser zaloUserInfo) {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setStatus(CustomerStatus.ACTIVE);
        customerModel.setFullName(zaloUserInfo.getName());
        customerModel.setUsername(zaloUserInfo.getId());
        if (zaloUserInfo.getPicture().getData() != null) {
            customerModel.setAvatar(zaloUserInfo.getPicture().getData().getUrl());
        }
        customerModel.setAffiliateCode(zaloUserInfo.getId());
        customerModel.setSearchText(buildSearchText(customerModel));
        return customerModel;
    }

    private String buildSearchText(CustomerModel customerModel) {
        StringBuilder builder = new StringBuilder();
        builder.append(customerModel.getUsername());
        builder.append(" ");
        builder.append(customerModel.getFullName());
        return builder.toString();

    }

    @Override
    public CustomerDTO updateByZaloProfile(ProfileRequest profileRequest, Long authenticatedUserId) {
        CustomerModel customerModel = findOne(authenticatedUserId);
        if (customerModel == null || customerModel.getStatus() != CustomerStatus.ACTIVE) {
            throw new ResourceNotFoundException(String.format("Not found Customer by id: %s", authenticatedUserId));
        }

        if (profileRequest.getToken() != null) {
            UserProfile userProfileResponse = zaloSocialService.getProfile(customerModel.getZaloAccessToken(), profileRequest.getToken());
            if (userProfileResponse.getData() == null) {
                if (userProfileResponse.getError() == 115) {
                    throw new BadRequestException("Code is invalid");
                }
                throw new ResourceNotFoundException("Can not get zalo user phoneNumber by token");
            }

            if (userProfileResponse.getData().getNumber() != null) {
                customerModel.setPhoneNumber(userProfileResponse.getData().getNumber().replaceFirst("^84", "0"));
            }
            customerModel = save(customerModel);
        } else {
            throw new BadRequestException("Not support for all code, phoneNumber");
        }

        log.info("------------------ CustomerController - Update - END ----------------");
        return customerConverter.toDTO(customerModel);
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {
        CustomerModel customerModel = findOne(customerDTO.getId());
        if (customerModel == null) {
            throw new ResourceNotFoundException(String.format("Not found Customer by id: %s", customerDTO.getId()));
        }
        customerConverter.mapDataToUpdate(customerModel, customerDTO);
        customerModel = save(customerModel);

        NhanhVNCustomerRequest nhanhVNCustomerRequest = new NhanhVNCustomerRequest();
        nhanhVNCustomerRequest.setName(customerModel.getFullName());
        nhanhVNCustomerRequest.setMobile(customerModel.getPhoneNumber());
        nhanhVNCustomerRequest.setCityName(customerModel.getCity());
        nhanhVNCustomerRequest.setDistrictName(customerModel.getDistrict());
        nhanhVNCustomerRequest.setWardName(customerModel.getWard());
        nhanhVNCustomerRequest.setAddress(customerModel.getAddress());
        nhanhVNCustomerService.createOrUpdate(nhanhVNCustomerRequest);

        return customerConverter.toDTO(customerModel);
    }

    @Override
    public void useAffiliateCode(String affiliateCode, Long id) {
        CustomerModel referredCustomer = customerRepository.findByAffiliateCode(affiliateCode);
        if (referredCustomer == null) {
            throw new ResourceNotFoundException(String.format("Not found referred Customer by affiliateCode: %s", affiliateCode));
        }

        CustomerModel affiliateCustomerModel = findOne(id);
        if (affiliateCustomerModel == null) {
            throw new ResourceNotFoundException(String.format("Not found use affiliate Customer by id: %s", id));
        }

        boolean isCreateCustomerAffiliateDetailModel = true;

        if (orderService.existsByCustomerId(id)) {
            throw new BadRequestException("You had any order");
        } else if (customerAffiliateDetailService.existsByAffiliateCustomerId(id)) {
            isCreateCustomerAffiliateDetailModel = false;
        }

        if (isCreateCustomerAffiliateDetailModel) {
            CustomerAffiliateDetailModel customerAffiliateDetailModel = new CustomerAffiliateDetailModel();
            customerAffiliateDetailModel.setAffiliateCustomerId(affiliateCustomerModel.getId());
            customerAffiliateDetailModel.setAffiliateCustomerFullName(affiliateCustomerModel.getFullName());
            customerAffiliateDetailModel.setAffiliateCustomerAvatar(affiliateCustomerModel.getAvatar());
            customerAffiliateDetailModel.setReferredCustomerId(referredCustomer.getId());
            customerAffiliateDetailModel.setReferredCustomerFullName(referredCustomer.getFullName());
            customerAffiliateDetailModel.setReferredCustomerAvatar(referredCustomer.getAvatar());

            createGifts(referredCustomer, affiliateCustomerModel);

            customerAffiliateDetailService.save(customerAffiliateDetailModel);
        }
    }

    private void createGifts(CustomerModel referredCustomer, CustomerModel affiliateCustomerModel) {
        List<SaleModel> saleModels = saleService.findIntroduceCustomerSaleCampaign();
        if (!CollectionUtils.isEmpty(saleModels)) {
            for (SaleModel saleModel : saleModels) {
                if (saleModel.getIntroduceCustomerScope() == IntroduceCustomerScope.ALL
                        || saleModel.getIntroduceCustomerScope() == IntroduceCustomerScope.AFFILIATE_CUSTOMER) {
                    GiftModel affiliateCustomerGift = new GiftModel();
                    affiliateCustomerGift.setCustomer(affiliateCustomerModel);
                    affiliateCustomerModel.getGifts().add(affiliateCustomerGift);
                    affiliateCustomerGift.setSale(saleModel);
                    saleModel.getGifts().add(affiliateCustomerGift);
                    affiliateCustomerGift.setType(GiftType.INTRODUCE_CUSTOMER);
                    affiliateCustomerGift.setExpiredDate(saleModel.getEndDate());

                    giftService.save(affiliateCustomerGift);
                }
            }
        }
    }

    @Override
    public Map<String, Integer> findTotalYourOrderStatus(Long authenticatedUserId) {
        List<OrderStatus> paymentPendingStatus = new ArrayList<>(
                Arrays.asList(OrderStatus.NEW, OrderStatus.CONFIRMED, OrderStatus.CONFIRMING, OrderStatus.CUSTOMER_CONFIRMING));
        List<OrderStatus> shippingPendingStatus = new ArrayList<>(
                Arrays.asList(OrderStatus.PACKING, OrderStatus.PACKED, OrderStatus.CHANGE_DEPOT, OrderStatus.PICKUP));
        List<OrderStatus> shippingStatus = new ArrayList<>(Arrays.asList(OrderStatus.SHIPPING));
        List<OrderStatus> cancelStatus = new ArrayList<>(Arrays.asList(OrderStatus.FAILED, OrderStatus.CANCELED, OrderStatus.ABORTED,
                OrderStatus.CARRIER_CANCELED, OrderStatus.SOLD_OUT, OrderStatus.RETURNING, OrderStatus.RETURNED));

        Map<String, Integer> map = new HashMap<>();
        map.put("paymentPending", orderService.countByCustomerIdAndStatusIn(authenticatedUserId, paymentPendingStatus));
        map.put("shippingPending", orderService.countByCustomerIdAndStatusIn(authenticatedUserId, shippingPendingStatus));
        map.put("shipping", orderService.countByCustomerIdAndStatusIn(authenticatedUserId, shippingStatus));
        map.put("cancel", orderService.countByCustomerIdAndStatusIn(authenticatedUserId, cancelStatus));

        log.info("------------------ Get total your order status - END ----------------");
        return map;
    }

    @Override
    public void createGitfForReferredCustomer(Long affiliateCustomerId) {
        List<CustomerModel> customerModels = customerRepository.findCustomerByAffiliateCustomerId(affiliateCustomerId);
        if (!CollectionUtils.isEmpty(customerModels)) {
            SaleModel saleModel = saleService.findIntroduceCustomerSaleCampaignByScopeIn(
                    Arrays.asList(IntroduceCustomerScope.REFERRED_CUSTOMER.name(), IntroduceCustomerScope.ALL.name()));
            if (saleModel != null) {
                for (CustomerModel customerModel : customerModels) {
                    GiftModel referredCustomerGift = new GiftModel();
                    referredCustomerGift.setCustomer(customerModel);
                    customerModel.getGifts().add(referredCustomerGift);
                    referredCustomerGift.setSale(saleModel);
                    saleModel.getGifts().add(referredCustomerGift);
                    referredCustomerGift.setType(GiftType.INTRODUCE_CUSTOMER);
                    referredCustomerGift.setExpiredDate(saleModel.getEndDate());

                    giftService.save(referredCustomerGift);
                }
            }

        }
    }

    @Override
    public List<CustomerDTO> FindCustomerByReferredCustomerId(Long referredCustomerId) {
        List<CustomerModel> customerModels = customerRepository.FindCustomerByReferredCustomerId(referredCustomerId);
        if (!CollectionUtils.isEmpty(customerModels)) {
            return customerConverter.toBasicDTOs(customerModels);
        }
        return null;
    }

    @Override
    public Boolean isNewCustomer(Long customerId) {
        return customerRepository.isNewCustomer(customerId);
    }

    @Override
    public List<String> findCustomerAffiliateAvatarByReferredCustomerId(Long referredCustomerId) {
        return customerRepository.findCustomerAffiliateAvatarByReferredCustomerId(referredCustomerId);
    }

}
