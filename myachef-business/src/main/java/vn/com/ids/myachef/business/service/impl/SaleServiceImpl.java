package vn.com.ids.myachef.business.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.SaleConverter;
import vn.com.ids.myachef.business.converter.SaleDetailConverter;
import vn.com.ids.myachef.business.dto.SaleDTO;
import vn.com.ids.myachef.business.dto.SaleDetailDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.GiftService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.SaleDetailService;
import vn.com.ids.myachef.business.service.SaleService;
import vn.com.ids.myachef.dao.criteria.SaleCriteria;
import vn.com.ids.myachef.dao.criteria.builder.SaleSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.IntroduceCustomerScope;
import vn.com.ids.myachef.dao.enums.PromotionType;
import vn.com.ids.myachef.dao.enums.SaleScope;
import vn.com.ids.myachef.dao.enums.SaleType;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.ProductConfigModel;
import vn.com.ids.myachef.dao.model.SaleDetailModel;
import vn.com.ids.myachef.dao.model.SaleModel;
import vn.com.ids.myachef.dao.repository.SaleRepository;

@Transactional
@Service
@Slf4j
public class SaleServiceImpl extends AbstractService<SaleModel, Long> implements SaleService {

    private SaleRepository saleRepository;

    protected SaleServiceImpl(SaleRepository saleRepository) {
        super(saleRepository);
        this.saleRepository = saleRepository;
    }

    @Autowired
    private SaleConverter saleConverter;

    @Autowired
    private SaleDetailConverter saleDetailConverter;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private SaleDetailService saleDetailService;

    @Autowired
    private GiftService giftService;

    @Autowired
    private CustomerService customerService;

    private Specification<SaleModel> buildSpecification(SaleCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new SaleSpecificationBuilder(root, criteriaBuilder) //
                .setName(criteria.getName()) //
                .setStatus(criteria.getStatus()) //
                .setTimeFrame(criteria.getFrom(), criteria.getTo()) //
                .setType(criteria.getType()) //
                .setSaleScope(criteria.getSaleScope()) //
                .setPromotionType(criteria.getPromotionType()) //
                .setCode(criteria.getCode()) //
                .setIsIntroduceCustomer(criteria.getIsIntroduceCustomer()) //
                .setIntroduceCustomerScope(criteria.getIntroduceCustomerScope()) //
                .build();
    }

    @Override
    public Page<SaleModel> search(SaleCriteria saleCriteria) {
        Pageable pageable = buildPageable(saleCriteria);
        Specification<SaleModel> specification = buildSpecification(saleCriteria);
        return saleRepository.findAll(specification, pageable);
    }

    @Override
    public SaleDTO create(SaleDTO saleDTO, Long customerId) {
        if (!saleDTO.getEndDate().isAfter(saleDTO.getStartDate())) {
            throw new BadRequestException("End date must be greater than the start date");
        }

        if (saleDTO.getType() == SaleType.SUBSCRIPTION_VOUCHER) {
            throw new BadRequestException("Can not create sale with type = 'SUBSCRIPTION_VOUCHER'");
        }

        if (saleDTO.isIntroduceCustomer() && saleDTO.getIntroduceCustomerScope() == null) {
            throw new BadRequestException("Field introduceCustomerScope Can not be null with isIntroduceCustomer = true");
        }

        if (saleDTO.getType() == SaleType.SALE_FOR_NEW_CUSTOMER && existSaleForCustomerActiveInTimeFrame(saleDTO.getStartDate(), saleDTO.getEndDate())) {
            throw new BadRequestException("Sale for customer in time frame is already exist");
        }

        validateTimeFrameForCreate(saleDTO);

        SaleModel saleModel = saleConverter.toModel(saleDTO);
        saleModel.setStatus(Status.ACTIVE);
        saleModel.setTotalQuantityUsed(0l);
        switch (saleDTO.getType()) {
        case FLASH_SALE:
        case SALE_CAMPAIGN:
        case SALE_FOR_NEW_CUSTOMER:
            saleModel.setUpdatePrice(true);
            saleModel.setRevertPrice(true);
            createSaleDetail(saleModel, saleDTO);
            break;
        case VOUCHER:
            createVoucher(saleModel, saleDTO);
            break;

        default:
            break;
        }

        saleRepository.save(saleModel);

        if (saleModel.isIntroduceCustomer()) {
            if (saleModel.getIntroduceCustomerScope() == IntroduceCustomerScope.ALL) {
                saleRepository.disableAllAnotherIntroduceCustomer(saleModel.getId());
            } else {
                saleRepository.disableAllAnotherIntroduceCustomerByScopeIn(
                        Arrays.asList(saleModel.getIntroduceCustomerScope().name(), IntroduceCustomerScope.ALL.name()), saleModel.getId());
            }
        }
        log.info("------------------ Create - END ----------------");
        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        return saleConverter.toDTO(saleModel, isNewCustomer);
    }

    private void validateTimeFrameForCreate(SaleDTO saleDTO) {
        if (saleDTO.getType() == SaleType.SALE_CAMPAIGN || saleDTO.getType() == SaleType.FLASH_SALE) {
            boolean saleExists = false;

            if (saleDTO.getSaleScope() == SaleScope.ALL) {
                saleExists = isExistByTimeFrame(saleDTO.getStartDate(), saleDTO.getEndDate());
            } else if (saleDTO.getSaleScope() == SaleScope.PRODUCT) {
                List<Long> productIds = null;
                if (!CollectionUtils.isEmpty(saleDTO.getSaleDetailDTOs())) {
                    productIds = saleDTO.getSaleDetailDTOs().stream().map(SaleDetailDTO::getProductId).collect(Collectors.toList());
                }
                if (!CollectionUtils.isEmpty(productIds)) {
                    saleExists = isExistByTimeFrameAndProductIds(saleDTO.getStartDate(), saleDTO.getEndDate(), productIds);
                } else {
                    saleExists = isExistByTimeFrame(saleDTO.getStartDate(), saleDTO.getEndDate());
                }
            }

            if (saleExists) {
                throw new BadRequestException("In this time frame, another sale already exists");
            }
        }
    }

    private void createVoucher(SaleModel saleModel, SaleDTO saleDTO) {
        String message = saleConverter.validateVoucher(saleDTO);
        if (StringUtils.hasText(message)) {
            throw new BadRequestException(message);
        }

        createSaleDetail(saleModel, saleDTO);

        save(saleModel);
    }

    private void createSaleDetail(SaleModel saleModel, SaleDTO saleDTO) {
        if (saleDTO.getSaleScope() == SaleScope.PRODUCT && !CollectionUtils.isEmpty(saleDTO.getSaleDetailDTOs())) {
            for (SaleDetailDTO saleDetailDTO : saleDTO.getSaleDetailDTOs()) {
                if (saleDetailDTO.getProductId() != null) {
                    ProductConfigModel productConfigModel = productConfigService.findOne(saleDetailDTO.getProductId());
                    if (productConfigModel != null) {
                        SaleDetailModel saleDetailModel = saleDetailConverter.toModel(saleDetailDTO, saleDTO.getType());
                        saleDetailModel.setId(null);
                        saleDetailModel.setTotalQuantityUsed(0l);
                        saleDetailModel.setProduct(productConfigModel);
                        productConfigModel.getSaleDetails().add(saleDetailModel);
                        saleDetailModel.setSale(saleModel);
                        saleModel.getSaleDetails().add(saleDetailModel);
                        saleDetailService.save(saleDetailModel);
                    } else {
                        log.error("Not found product with id: {}", saleDetailDTO.getProductId());
                    }
                } else {
                    throw new BadRequestException("Field productId can not be null");
                }
            }
        }
    }

    @Override
    public SaleDTO update(SaleModel saleModel, SaleDTO saleDTO, Long customerId) {
        if (!saleDTO.getEndDate().isAfter(saleDTO.getStartDate())) {
            throw new BadRequestException("End date must be greater than the start date");
        }

        if (saleDTO.getType() == SaleType.SUBSCRIPTION_VOUCHER) {
            throw new BadRequestException("Can not update sale with type = 'SUBSCRIPTION_VOUCHER'");
        }

        if (saleDTO.isIntroduceCustomer() && saleDTO.getIntroduceCustomerScope() == null) {
            throw new BadRequestException("Field introduceCustomerScope Can not be null with isIntroduceCustomer = true");
        }

        if (saleDTO.getType() == SaleType.SALE_FOR_NEW_CUSTOMER
                && existSaleForCustomerActiveInTimeFrameIgnoreId(saleDTO.getStartDate(), saleDTO.getEndDate(), saleModel.getId())) {
            throw new BadRequestException("Sale for customer in time frame is already exist");
        }

        validateTimeFrameForUpdate(saleDTO, saleModel.getId());

        saleModel.getSaleDetails().clear();

        Status currentStatus = saleModel.getStatus();
        saleConverter.update(saleModel, saleDTO);
        switch (saleDTO.getType()) {
        case FLASH_SALE:
        case SALE_CAMPAIGN:
        case SALE_FOR_NEW_CUSTOMER:
            if (currentStatus == Status.IN_ACTIVE && saleDTO.getStatus() == Status.ACTIVE && saleDTO.getPromotionType() == PromotionType.SALE) {
                saleModel.setUpdatePrice(true);
                saleModel.setRevertPrice(true);
            }
            createSaleDetail(saleModel, saleDTO);
            break;
        case VOUCHER:
            createVoucher(saleModel, saleDTO);
            break;

        default:
            break;
        }

        saleRepository.save(saleModel);

        if (saleModel.isIntroduceCustomer() && saleModel.getStatus() == Status.ACTIVE) {
            if (saleModel.getIntroduceCustomerScope() == IntroduceCustomerScope.ALL) {
                saleRepository.disableAllAnotherIntroduceCustomer(saleModel.getId());
            } else {
                saleRepository.disableAllAnotherIntroduceCustomerByScopeIn(
                        Arrays.asList(saleModel.getIntroduceCustomerScope().name(), IntroduceCustomerScope.ALL.name()), saleModel.getId());
            }
        }

        log.info("------------------ Update - END ----------------");
        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        return saleConverter.toDTO(saleModel, isNewCustomer);
    }

    private void validateTimeFrameForUpdate(SaleDTO saleDTO, Long id) {
        if (saleDTO.getType() == SaleType.SALE_CAMPAIGN || saleDTO.getType() == SaleType.FLASH_SALE) {
            boolean saleExists = false;

            if (saleDTO.getSaleScope() == SaleScope.ALL) {
                saleExists = isExistByTimeFrameIgnoreId(saleDTO.getStartDate(), saleDTO.getEndDate(), id);
            } else if (saleDTO.getSaleScope() == SaleScope.PRODUCT) {
                List<Long> productIds = null;
                if (!CollectionUtils.isEmpty(saleDTO.getSaleDetailDTOs())) {
                    productIds = saleDTO.getSaleDetailDTOs().stream().map(SaleDetailDTO::getProductId).collect(Collectors.toList());
                }
                if (!CollectionUtils.isEmpty(productIds)) {
                    saleExists = isExistByTimeFrameAndProductIdsIgnoreId(saleDTO.getStartDate(), saleDTO.getEndDate(), productIds, id);
                } else {
                    saleExists = isExistByTimeFrameIgnoreId(saleDTO.getStartDate(), saleDTO.getEndDate(), id);
                }
            }

            if (saleExists) {
                throw new BadRequestException("In this time frame, another sale already exists");
            }
        }
    }

    @Override
    public List<SaleDTO> getAllFlashSaleActive(Long customerId) {
        List<SaleModel> saleModels = saleRepository.getAllFlashSaleActive();
        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        return saleConverter.toDTOs(saleModels, isNewCustomer);
    }

    @Override
    public List<SaleDTO> findVoucherByConditon(List<String> nhanhVnProductIds, Long customerId, double amount) {
        List<SaleModel> saleModels = saleRepository.findVoucherActive();
        List<SaleDTO> saleDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(saleModels)) {
            List<Long> saleIdByNhanhByNhanhVnProductIdIn = saleDetailService.findSaleIdByNhanhByNhanhVnProductIdIn(nhanhVnProductIds);
            for (SaleModel saleModel : saleModels) {
                SaleDTO saleDTO = saleConverter.toDTOAndCheckCanUse(saleModel, saleIdByNhanhByNhanhVnProductIdIn, customerId, amount);
                if (saleDTO != null) {
                    saleDTOs.add(saleDTO);
                }
            }
        }

        saleDTOs.addAll(giftService.findGiftByCustomerIdAndCondition(customerId, nhanhVnProductIds, amount));

        log.info("------------------ Get all voucher by condition - END ----------------");
        return saleDTOs;
    }

    @Override
    public List<SaleDTO> findByCode(String code, List<String> nhanhVnProductIds, Long customerId, double amount) {
        List<SaleModel> saleModels = saleRepository.findByCode(code);

        List<SaleDTO> saleDTOs = new ArrayList<>();

        if (!CollectionUtils.isEmpty(saleModels)) {
            List<Long> saleIdByNhanhByNhanhVnProductIdIn = saleDetailService.findSaleIdByNhanhByNhanhVnProductIdIn(nhanhVnProductIds);
            for (SaleModel saleModel : saleModels) {
                if (saleModel.getType() == SaleType.VOUCHER && saleModel.getStatus() == Status.ACTIVE && saleModel.getEndDate().isAfter(LocalDateTime.now())
                        && !saleModel.isIntroduceCustomer()) {
                    SaleDTO saleDTO = saleConverter.toDTOAndCheckCanUse(saleModel, saleIdByNhanhByNhanhVnProductIdIn, customerId, amount);
                    if (saleDTO != null) {
                        saleDTOs.add(saleDTO);
                    }
                }
            }
        }
        log.info("------------------ Search voucher by code - END ----------------");
        return saleDTOs;
    }

    @Override
    public List<SaleModel> findIntroduceCustomerSaleCampaign() {
        return saleRepository.findIntroduceCustomerSaleCampaign();
    }

    @Override
    public List<SaleModel> findGiftByCustomerId(Long customerId) {
        return saleRepository.findGiftByCustomerId(customerId);
    }

    @Override
    public SaleModel findIntroduceCustomerSaleCampaignByScopeIn(List<String> scopes) {
        return saleRepository.findIntroduceCustomerSaleCampaignByScopeIn(scopes);
    }

    @Override
    public SaleDTO findById(Long id, Long customerId) {
        SaleModel saleModel = findOne(id);
        if (saleModel == null) {
            throw new ResourceNotFoundException("Not found sale with id: " + id);
        }

        log.info("------------------ Find by id - END ----------------");
        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        return saleConverter.toDTO(saleModel, isNewCustomer);
    }

    @Override
    public boolean isExistByTimeFrame(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.isExistByTimeFrame(startDate, endDate);
    }

    @Override
    public boolean isExistByTimeFrameAndProductIds(LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds) {
        return saleRepository.isExistByTimeFrameAndProductIds(startDate, endDate, productIds);
    }

    @Override
    public List<Long> findProductIdsExistByTimeFrame(LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("------------------ Find product ids exist in sale by time frame - END ----------------");
        return saleRepository.findProductIdsExistByTimeFrame(fromDate, toDate);
    }

    @Override
    public void updateProductPrice() {
        List<SaleModel> saleStarts = saleRepository.findSaleStartByTimeFrame();
        if (!CollectionUtils.isEmpty(saleStarts)) {
            for (SaleModel saleModel : saleStarts) {
                updateProductPriceBySale(saleModel);
                saleModel.setUpdatePrice(false);

                saveAll(saleStarts);
            }
        }

        List<SaleModel> saleEnds = saleRepository.findSaleEndByTimeFrame();
        if (!CollectionUtils.isEmpty(saleEnds)) {
            for (SaleModel saleModel : saleEnds) {
                revertProductPriceBySale(saleModel);
                saleModel.setRevertPrice(false);

                saveAll(saleEnds);
            }
        }
    }

    @Override
    public void updateProductPriceBySale(SaleModel saleModel) {
        log.info("------------------ updateProductPriceBySale scheduler - START ----------------");
        if (saleModel != null && saleModel.getPromotionType() == PromotionType.SALE) {
            switch (saleModel.getSaleScope()) {
            case ALL:
                if (saleModel.getDiscount() != null && saleModel.getDiscount() > 0) {
                    if (saleModel.getType() == SaleType.SALE_FOR_NEW_CUSTOMER) {
                        productConfigService.updateSaleNewCustomerPriceByDiscountAndQuantityAllProduct(saleModel.getDiscount(),
                                saleModel.getMaxQuantityUseInUser());
                    } else {
                        productConfigService.updatePriceByDiscountAndQuantityAndBackUpOldPriceAllProduct(saleModel.getDiscount(),
                                saleModel.getMaxQuantityUseInUser());
                    }
                } else if (saleModel.getDiscountPercent() != null && saleModel.getDiscountPercent() > 0) {
                    if (saleModel.getType() == SaleType.SALE_FOR_NEW_CUSTOMER) {
                        productConfigService.updateSaleNewCustomerPriceByDiscountPercentAndQuantityAllProduct(saleModel.getDiscountPercent(),
                                saleModel.getMaxPromotion(), saleModel.getMaxQuantityUseInUser());
                    } else {
                        productConfigService.updatePriceByDiscountPercentAndQuantityAndBackUpOldPriceAllProduct(saleModel.getDiscountPercent(),
                                saleModel.getMaxPromotion(), saleModel.getMaxQuantityUseInUser());
                    }
                }
                break;

            case PRODUCT:
                if (!CollectionUtils.isEmpty(saleModel.getSaleDetails())) {
                    for (SaleDetailModel saleDetailModel : saleModel.getSaleDetails()) {
                        ProductConfigModel productConfig = saleDetailModel.getProduct();
                        if (productConfig != null) {
                            double updateDate = productConfig.getPrice();
                            if (productConfig.getSalePriceBackup() != -1) {
                                updateDate = productConfig.getSalePriceBackup();
                            }
                            if (saleDetailModel.getDiscount() != null && saleDetailModel.getDiscount() > 0) {
                                updateDate = updateDate - saleDetailModel.getDiscount() < 0 ? 0 : updateDate - saleDetailModel.getDiscount();
                            }
                            if (saleDetailModel.getDiscountPercent() != null && saleDetailModel.getDiscountPercent() > 0) {
                                double price = updateDate * saleDetailModel.getDiscountPercent() / 100;
                                if (saleDetailModel.getMaxPromotion() != null && saleDetailModel.getMaxPromotion() > 0) {
                                    if (price > saleDetailModel.getMaxPromotion()) {
                                        updateDate = updateDate - saleDetailModel.getMaxPromotion().doubleValue();
                                    } else {
                                        updateDate = updateDate - price < 0 ? 0 : updateDate - price;
                                    }
                                } else {
                                    updateDate = updateDate - price < 0 ? 0 : updateDate - price;
                                }
                            }

                            if (saleModel.getType() == SaleType.SALE_FOR_NEW_CUSTOMER) {
                                productConfig.setSaleNewCustomerPrice(updateDate);
                                productConfig.setSaleNewCustomerQuantity(saleDetailModel.getMaxQuantityUseInUser());
                            } else {
                                productConfig.setSalePriceBackup(productConfig.getPrice());
                                productConfig.setPrice(updateDate);
                                productConfig.setSaleQuantity(saleDetailModel.getMaxQuantityUseInUser());
                            }
                        }
                    }

                    saleDetailService.saveAll(saleModel.getSaleDetails());
                }
                break;

            default:
                break;
            }
        }
        log.info("------------------ updateProductPriceBySale scheduler - END ----------------");
    }

    @Override
    public void revertProductPriceBySale(SaleModel saleModel) {
        log.info("------------------ revertProductPriceBySale scheduler - START ----------------");
        if (saleModel != null) {
            switch (saleModel.getSaleScope()) {
            case ALL:
                if (saleModel.getType() == SaleType.SALE_FOR_NEW_CUSTOMER) {
                    productConfigService.updateSaleNewCustomerPriceAllProduct(-1);
                } else {
                    productConfigService.revertPriceAndUpdateSalePriceBackupAllProduct(-1);
                }
                break;

            case PRODUCT:
                List<Long> productIds = saleDetailService.findProductIdBySaleId(saleModel.getId());
                if (!CollectionUtils.isEmpty(productIds)) {
                    if (saleModel.getType() == SaleType.SALE_FOR_NEW_CUSTOMER) {
                        productConfigService.updateSaleNewCustomerPriceByIdIn(-1, productIds);
                    } else {
                        productConfigService.revertPriceAndUpdateSalePriceBackupByIdIn(-1, productIds);
                    }
                }
                break;

            default:
                break;
            }
        }
        log.info("------------------ revertProductPriceBySale scheduler - END ----------------");
    }

    @Override
    public List<Long> findProductIdsExistByTimeFrameIgnoreBySaleId(LocalDateTime fromDate, LocalDateTime toDate, Long saleId) {
        return saleRepository.findProductIdsExistByTimeFrameIgnoreBySaleId(fromDate, toDate, saleId);
    }

    @Override
    public boolean existByTimeFrameAndScopeAll(LocalDateTime fromDate, LocalDateTime toDate) {
        return saleRepository.existByTimeFrameAndScopeAll(fromDate, toDate);
    }

    @Override
    public boolean existByTimeFrameAndScopeAllIgnoreSaleId(LocalDateTime fromDate, LocalDateTime toDate, Long saleId) {
        return saleRepository.existByTimeFrameAndScopeAllIgnoreSaleId(fromDate, toDate, saleId);
    }

    @Override
    public boolean isExistByTimeFrameIgnoreId(LocalDateTime startDate, LocalDateTime endDate, Long id) {
        return saleRepository.isExistByTimeFrameIgnoreId(startDate, endDate, id);
    }

    @Override
    public boolean isExistByTimeFrameAndProductIdsIgnoreId(LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds, Long id) {
        return saleRepository.isExistByTimeFrameAndProductIdsIgnoreId(startDate, endDate, productIds, id);
    }

    @Override
    public void updateProductPriceBySale(ProductConfigModel productConfig, Double newPrice) {
        List<SaleDetailModel> saleDetailModels = saleDetailService.findByProductId(productConfig.getId());
        if (!CollectionUtils.isEmpty(saleDetailModels)) {
            for (SaleDetailModel saleDetailModel : saleDetailModels) {
                double updateDate = newPrice;
                if (saleDetailModel.getDiscount() != null && saleDetailModel.getDiscount() > 0) {
                    updateDate = newPrice - saleDetailModel.getDiscount() < 0 ? 0 : newPrice - saleDetailModel.getDiscount();
                }
                if (saleDetailModel.getDiscountPercent() != null && saleDetailModel.getDiscountPercent() > 0) {
                    double discountPrice = newPrice * saleDetailModel.getDiscountPercent() / 100;
                    if (saleDetailModel.getMaxPromotion() != null && saleDetailModel.getMaxPromotion() > 0) {
                        if (discountPrice > saleDetailModel.getMaxPromotion()) {
                            updateDate = newPrice - saleDetailModel.getMaxPromotion().doubleValue();
                        } else {
                            updateDate = newPrice - discountPrice < 0 ? 0 : newPrice - discountPrice;
                        }
                    } else {
                        updateDate = newPrice - discountPrice < 0 ? 0 : newPrice - discountPrice;
                    }
                }

                if (saleDetailModel.getSale().getType() == SaleType.SALE_FOR_NEW_CUSTOMER) {
                    productConfig.setPrice(newPrice);
                    productConfig.setSaleNewCustomerPrice(updateDate);
                } else {
                    productConfig.setSalePriceBackup(newPrice);
                    productConfig.setPrice(updateDate);
                }
            }
        }

        List<SaleModel> saleModels = saleRepository.findSaleAllInProgress();
        if (!CollectionUtils.isEmpty(saleModels)) {
            for (SaleModel saleModel : saleModels) {
                double updateDate = newPrice;
                if (saleModel.getDiscount() != null && saleModel.getDiscount() > 0) {
                    updateDate = newPrice - saleModel.getDiscount();
                } else if (saleModel.getDiscountPercent() != null && saleModel.getDiscountPercent() > 0) {
                    double price = newPrice * saleModel.getDiscountPercent() / 100;
                    if (saleModel.getMaxPromotion() != null && saleModel.getMaxPromotion() > 0) {
                        if (price > saleModel.getMaxPromotion()) {
                            updateDate = newPrice - saleModel.getMaxPromotion().doubleValue();
                        } else {
                            updateDate = newPrice - price < 0 ? 0 : newPrice - price;
                        }
                    } else {
                        updateDate = newPrice - price < 0 ? 0 : newPrice - price;
                    }
                }

                if (saleModel.getType() == SaleType.SALE_FOR_NEW_CUSTOMER) {
                    productConfig.setPrice(newPrice);
                    productConfig.setSaleNewCustomerPrice(updateDate);
                } else {
                    productConfig.setSalePriceBackup(newPrice);
                    productConfig.setPrice(updateDate);
                }
            }
        }
    }

    @Override
    public boolean existSaleForCustomerActiveInTimeFrame(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.existSaleForCustomerActiveInTimeFrame(startDate, endDate);
    }

    @Override
    public boolean existSaleForCustomerActiveInTimeFrameIgnoreId(LocalDateTime startDate, LocalDateTime endDate, Long saleId) {
        return saleRepository.existSaleForCustomerActiveInTimeFrameIgnoreId(startDate, endDate, saleId);
    }

    @Override
    public List<SaleModel> findSaleActiveScopeAllOrByProductId(Long productId) {
        return saleRepository.findSaleActiveScopeAllOrByProductId(productId);
    }
}
