package vn.com.ids.myachef.business.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.SubscriptionCustomerDetailConverter;
import vn.com.ids.myachef.business.dto.SubscriptionCustomerDetailDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.CustomerCoinHistoryService;
import vn.com.ids.myachef.business.service.SubscriptionCustomerDetailService;
import vn.com.ids.myachef.dao.criteria.SubscriptionCustomerDetailCriteria;
import vn.com.ids.myachef.dao.criteria.builder.SubscriptionCustomerDetailSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryScope;
import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryType;
import vn.com.ids.myachef.dao.enums.PaymentStatus;
import vn.com.ids.myachef.dao.enums.SubscriptionCustomerType;
import vn.com.ids.myachef.dao.enums.SubscriptionDetailType;
import vn.com.ids.myachef.dao.model.CustomerCoinHistoryModel;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.model.SubscriptionCustomerDetailModel;
import vn.com.ids.myachef.dao.model.SubscriptionDetailModel;
import vn.com.ids.myachef.dao.repository.SubscriptionCustomerDetailRepository;

@Service
@Transactional
@Slf4j
public class SubscriptionCustomerDetailServiceImpl extends AbstractService<SubscriptionCustomerDetailModel, Long> implements SubscriptionCustomerDetailService {

    private SubscriptionCustomerDetailRepository subscriptionCustomerDetailRepository;

    protected SubscriptionCustomerDetailServiceImpl(SubscriptionCustomerDetailRepository subscriptionCustomerDetailRepository) {
        super(subscriptionCustomerDetailRepository);
        this.subscriptionCustomerDetailRepository = subscriptionCustomerDetailRepository;
    }

    @Autowired
    private SubscriptionCustomerDetailConverter subscriptionCustomerDetailConverter;

    @Autowired
    private CustomerCoinHistoryService customerCoinHistoryService;

    @Override
    public boolean existsSubscriptionByCustomerId(Long customerId) {
        return subscriptionCustomerDetailRepository.existsSubscriptionByCustomerId(customerId);
    }

    @Override
    public SubscriptionCustomerDetailModel findByPaymentStatusNotAndCustomerId(PaymentStatus paid, Long customerId) {
        return subscriptionCustomerDetailRepository.findByPaymentStatusNotAndCustomerId(paid, customerId);
    }

    private Specification<SubscriptionCustomerDetailModel> buildSpecification(SubscriptionCustomerDetailCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new SubscriptionCustomerDetailSpecificationBuilder(root, criteriaBuilder) //
                .setTimeFrame(criteria.getFrom(), criteria.getTo()) //
                .setPaymentStatus(criteria.getPaymentStatus()) //
                .setPaymentType(criteria.getPaymentType()) //
                .setSubscription(criteria.getSubscriptionId()) //
                .setCustomer(criteria.getCustomerId()) //
                .setType(criteria.getType()) //
                .build(); //
    }

    @Override
    public Page<SubscriptionCustomerDetailModel> search(SubscriptionCustomerDetailCriteria criteria) {
        Pageable pageable = buildPageable(criteria);
        Specification<SubscriptionCustomerDetailModel> specification = buildSpecification(criteria);
        return subscriptionCustomerDetailRepository.findAll(specification, pageable);
    }

    @Override
    public SubscriptionCustomerDetailDTO uploadBill(Long subscriptionCustomerDetailId, String billUrl, HttpServletRequest request) {
        if (!StringUtils.hasText(billUrl)) {
            throw new ResourceNotFoundException("Field billUrl is required");
        }

        SubscriptionCustomerDetailModel subscriptionCustomerDetailModel = findOne(subscriptionCustomerDetailId);
        if (subscriptionCustomerDetailModel == null) {
            throw new ResourceNotFoundException("Not found subscriptionCustomerDetail with id: " + subscriptionCustomerDetailId);
        }

        subscriptionCustomerDetailModel.setBillImage(billUrl);

        save(subscriptionCustomerDetailModel);
        log.info("------------------ Upload bill - END ----------------");
        return subscriptionCustomerDetailConverter.toDTO(subscriptionCustomerDetailModel, request);
    }

    @Override
    public SubscriptionCustomerDetailDTO updateStatus(Long subscriptionCustomerDetailId, SubscriptionCustomerDetailDTO subscriptionCustomerDetailDTO,
            HttpServletRequest request) {
        SubscriptionCustomerDetailModel subscriptionCustomerDetailModel = findOne(subscriptionCustomerDetailId);

        if (subscriptionCustomerDetailModel.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BadRequestException("Can not update subscriptionCustomerDetail with paymentStatus = PAID");
        }

        if (subscriptionCustomerDetailModel == null || subscriptionCustomerDetailModel.getType() != SubscriptionCustomerType.BUY) {
            throw new ResourceNotFoundException("Not found subscriptionCustomerDetail or cannot update with type != BUY for subscriptionCustomerDetail id: "
                    + subscriptionCustomerDetailId);
        }

        boolean isBuySuccess = subscriptionCustomerDetailConverter.update(subscriptionCustomerDetailModel, subscriptionCustomerDetailDTO);

        if (isBuySuccess) {
            createGiftFromSubscriptionForCustomer(subscriptionCustomerDetailModel);
        }

        subscriptionCustomerDetailRepository.save(subscriptionCustomerDetailModel);
        return subscriptionCustomerDetailConverter.toDTO(subscriptionCustomerDetailModel, request);
    }

    private void createGiftFromSubscriptionForCustomer(SubscriptionCustomerDetailModel subscriptionCustomerDetailModel) {
        List<SubscriptionDetailModel> subscriptionDetails = subscriptionCustomerDetailModel.getSubscription().getSubscriptionDetails();
        CustomerModel customerModel = subscriptionCustomerDetailModel.getCustomer();
        if (!CollectionUtils.isEmpty(subscriptionDetails) && customerModel != null) {
            List<SubscriptionCustomerDetailModel> gifts = new ArrayList<>();

            for (SubscriptionDetailModel subscriptionDetailModel : subscriptionDetails) {
                if (subscriptionDetailModel.getType() == SubscriptionDetailType.COIN) {
                    customerModel.setCoin(subscriptionDetailModel.getCoin());

                    CustomerCoinHistoryModel coinHistory = new CustomerCoinHistoryModel();
                    coinHistory.setCustomerFullName(customerModel.getFullName());
                    coinHistory.setCustomerId(customerModel.getId());
                    coinHistory.setQuantity(subscriptionDetailModel.getCoin());
                    coinHistory.setScope(CustomerCoinHistoryScope.SUBSCRIPTION);
                    coinHistory.setScopeId(subscriptionDetailModel.getId());
                    coinHistory.setType(CustomerCoinHistoryType.RECEIVE);

                    customerCoinHistoryService.save(coinHistory);
                } else {
                    SubscriptionCustomerDetailModel gift = new SubscriptionCustomerDetailModel();
                    mapDataForGift(gift, subscriptionDetailModel, customerModel);
                    gift.setExpiredDate(subscriptionCustomerDetailModel.getExpiredDate());
                    gifts.add(gift);
                }
            }

            subscriptionCustomerDetailRepository.saveAll(gifts);
        }
    }

    private void mapDataForGift(SubscriptionCustomerDetailModel gift, SubscriptionDetailModel subscriptionDetailModel, CustomerModel customerModel) {
        gift.setType(SubscriptionCustomerType.GIFT);
        gift.setGiftQuantity(subscriptionDetailModel.getQuantity() == null ? 0 : subscriptionDetailModel.getQuantity());
        gift.setGiftTotalUsed(0);
        gift.setGiftDescription(subscriptionDetailModel.getDescription());
        gift.setLimit(subscriptionDetailModel.getLimit());

        if (subscriptionDetailModel.getType() == SubscriptionDetailType.INTRODUCE_GIFT_MONEY
                || subscriptionDetailModel.getType() == SubscriptionDetailType.GIFT_MONEY) {
            gift.setGiftQuantity(subscriptionDetailModel.getGiftMoney().intValue());
        }

        gift.setCustomer(customerModel);
        customerModel.getSubscriptionCustomerDetails().add(gift);
        gift.setSubscriptionDetail(subscriptionDetailModel);
        subscriptionDetailModel.getSubscriptionCustomerDetails().add(gift);
    }

    @Override
    public List<SubscriptionCustomerDetailDTO> findYourGift(Long customerId, HttpServletRequest request) {
        List<SubscriptionCustomerDetailModel> subscriptionCustomerDetailModels = subscriptionCustomerDetailRepository.findYourGift(customerId);
        log.info("------------------ Find your gift - END ----------------");
        return subscriptionCustomerDetailConverter.toGiftDTOs(subscriptionCustomerDetailModels, request);
    }

    @Override
    public List<SubscriptionCustomerDetailModel> findGiftCanUse(Double totalAmount, Long customerId, List<Long> cartIds, String nhanhVnProductId,
            int quantity) {
        List<SubscriptionCustomerDetailModel> subscriptionCustomerDetailModels = subscriptionCustomerDetailRepository.findGiftCanUse(totalAmount, customerId);
        // List<String> giftConditionProductNhanhVnIds = new ArrayList<>();
        // List<String> giftConditionProductCategoryNhanhVnIds = new ArrayList<>();
        // if (!CollectionUtils.isEmpty(subscriptionCustomerDetailModels)) {
        // for (SubscriptionCustomerDetailModel subscriptionCustomerDetailModel : subscriptionCustomerDetailModels) {
        // if (subscriptionCustomerDetailModel.getSubscriptionDetail() != null) {
        // if (subscriptionCustomerDetailModel.getSubscriptionDetail().getNhanhVnProductId() != null) {
        // giftConditionProductNhanhVnIds.add(subscriptionCustomerDetailModel.getSubscriptionDetail().getNhanhVnProductId());
        // } else if (subscriptionCustomerDetailModel.getSubscriptionDetail().getNhanhVnProductCategoryId() != null) {
        // giftConditionProductCategoryNhanhVnIds.add(subscriptionCustomerDetailModel.getSubscriptionDetail().getNhanhVnProductCategoryId());
        // }
        // }
        // }
        //
        // totalAmount = minusGiftPrice(giftConditionProductNhanhVnIds, giftConditionProductCategoryNhanhVnIds, totalAmount,
        // cartIds, nhanhVnProductId,
        // quantity);
        //
        // subscriptionCustomerDetailModels = subscriptionCustomerDetailRepository.findGiftCanUse(totalAmount, customerId);
        // }
        log.info("------------------ Get all gift by condition - START ----------------");
        return subscriptionCustomerDetailModels;
    }

    @Override
    public boolean existPaidBySubscriptionId(Long subscriptionId) {
        return subscriptionCustomerDetailRepository.existPaidBySubscriptionId(subscriptionId);
    }

    @Override
    public LocalDate findExpiredDateBySubscriptionIdAndCustomerId(Long subscriptionId, Long customerId) {
        return subscriptionCustomerDetailRepository.findExpiredDateBySubscriptionIdAndCustomerId(subscriptionId, customerId);
    }

    // private Double minusGiftPrice(List<String> giftConditionProductNhanhVnIds, List<String>
    // giftConditionProductCategoryNhanhVnIds, Double totalAmount,
    // List<Long> cartIds, String nhanhVnProductId, int quantity) {
    // if (!giftConditionProductNhanhVnIds.isEmpty() || !giftConditionProductCategoryNhanhVnIds.isEmpty()) {
    // Map<String, Double> mapPriceByNhanhVnId =
    // productConfigService.findPriceMapByNhanhVnIdsOrProductCategoryNhanhVnIds(giftConditionProductNhanhVnIds,
    // giftConditionProductCategoryNhanhVnIds);
    // if (!CollectionUtils.isEmpty(mapPriceByNhanhVnId)) {
    // List<CartModel> cartModels = cartService.findAllById(cartIds);
    // if (!CollectionUtils.isEmpty(cartModels)) {
    // for (CartModel cartModel : cartModels) {
    // Double price = mapPriceByNhanhVnId.get(cartModel.getNhanhVnProductId());
    // if (price != null) {
    // totalAmount -= price * cartModel.getQuantity();
    // }
    // }
    // } else if (nhanhVnProductId != null && quantity > 0) {
    // Double price = mapPriceByNhanhVnId.get(nhanhVnProductId);
    // if (price != null) {
    // totalAmount -= price * quantity;
    // }
    // }
    // }
    // }
    //
    // return totalAmount;
    // }
}
