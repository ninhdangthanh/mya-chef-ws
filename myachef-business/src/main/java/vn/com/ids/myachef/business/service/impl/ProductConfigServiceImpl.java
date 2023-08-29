package vn.com.ids.myachef.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.ProductConfigConverter;
import vn.com.ids.myachef.business.dto.ProductConfigDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductDataDetailResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductDetailResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnWebHooksData;
import vn.com.ids.myachef.business.service.NhanhVnAPIService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.SaleService;
import vn.com.ids.myachef.dao.criteria.ProductConfigCriteria;
import vn.com.ids.myachef.dao.criteria.builder.ProductConfigSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.ProductConfigModel;
import vn.com.ids.myachef.dao.repository.ProductConfigRepository;

@Transactional
@Service
@Slf4j
public class ProductConfigServiceImpl extends AbstractService<ProductConfigModel, Long> implements ProductConfigService {

    private ProductConfigRepository productConfigRepository;

    @Autowired
    private ProductConfigConverter productConfigConverter;

    protected ProductConfigServiceImpl(ProductConfigRepository productConfigRepository) {
        super(productConfigRepository);
        this.productConfigRepository = productConfigRepository;
    }

    @Autowired
    private NhanhVnAPIService nhanhVnAPIService;

    @Autowired
    private SaleService saleService;

    private Specification<ProductConfigModel> buildSpecification(ProductConfigCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new ProductConfigSpecificationBuilder(root, criteriaBuilder) //
                .setName(criteria.getName()) //
                .setNhanhVnId(criteria.getNhanhVnId()) //
                .setNhanhVnCategoryId(criteria.getNhanhVnCategoryId()) //
                .setStatus(criteria.getStatus()) //
                .setNewProduct(criteria.getIsNew()) //
                .setIgnoreRelatedProduct(criteria.getIgnoreRelatedProductNhanhVnId()) //
                .build(); //
    }

    @Override
    public Page<ProductConfigModel> search(ProductConfigCriteria productConfigCriteria) {
        Pageable pageable = buildPageable(productConfigCriteria);
        Specification<ProductConfigModel> specification = buildSpecification(productConfigCriteria);
        return productConfigRepository.findAll(specification, pageable);
    }

    @Override
    public List<ProductConfigModel> create(@Valid List<ProductConfigDTO> productConfigDTOs) {
        List<ProductConfigModel> productConfigModels = new ArrayList<>();
        if (!CollectionUtils.isEmpty(productConfigDTOs)) {
            List<String> nhanhVnProductIds = productConfigDTOs.stream().map(ProductConfigDTO::getNhanhVnId).collect(Collectors.toList());
            List<ProductConfigModel> productConfigOlds = productConfigRepository.findByNhanhVnIdIn(nhanhVnProductIds);
            Map<String, ProductConfigModel> mapProductConfigOldByNhanhVnId = new HashMap<>();
            if (!CollectionUtils.isEmpty(productConfigOlds)) {
                mapProductConfigOldByNhanhVnId = productConfigOlds.stream().collect(Collectors.toMap(ProductConfigModel::getNhanhVnId, product -> product));
            }
            for (ProductConfigDTO productConfigDTO : productConfigDTOs) {
                ProductConfigModel productConfigModel = mapProductConfigOldByNhanhVnId.get(productConfigDTO.getNhanhVnId());
                if (productConfigModel != null) {
                    if (productConfigModel.getSalePriceBackup() != -1 && productConfigDTO.getPrice() != productConfigModel.getSalePriceBackup()) {
                        saleService.updateProductPriceBySale(productConfigModel, productConfigDTO.getPrice());
                    }
                } else {
                    productConfigModel = productConfigConverter.toModel(productConfigDTO);
                    saleService.updateProductPriceBySale(productConfigModel, productConfigDTO.getPrice());
                }
                productConfigModel.setStatus(Status.ACTIVE);
                productConfigRepository.save(productConfigModel);
                productConfigModels.add(productConfigModel);
            }
        }
        return productConfigModels;
    }

    @Override
    public void updateProductInfoFromNhanhVn(NhanhVnWebHooksData data) {
        ProductConfigModel productConfigModel = productConfigRepository.findByNhanhVnId(data.getProductId());
        if (productConfigModel != null) {
            productConfigModel.setImage(data.getImage());
            productConfigModel.setName(data.getName());
            if (productConfigModel.getSalePriceBackup() != -1 && data.getPrice() != productConfigModel.getSalePriceBackup()) {
                saleService.updateProductPriceBySale(productConfigModel, data.getPrice());
            }

            if ("Inactive".equals(data.getStatus())) {
                productConfigModel.setStatus(Status.IN_ACTIVE);
            }

            // NhanhVnProductDetailResponse nhanhVnProductDetailResponse = nhanhVnAPIService.getById(data.getProductId());
            // if (nhanhVnProductDetailResponse != null) {
            // productConfigModel.setOldPrice(nhanhVnProductDetailResponse.getOldPrice());
            // productConfigModel.setImportPrice(nhanhVnProductDetailResponse.getImportPrice());
            // }

            productConfigRepository.save(productConfigModel);
        }
    }

    @Override
    public ProductConfigModel findByNhanhVnId(String nhanhVnProductId) {
        return productConfigRepository.findByNhanhVnId(nhanhVnProductId);
    }

    @Override
    public void removeNhanhVnCategoryIdByNhanhCategoryIds(List<String> nhanhVnIds) {
        productConfigRepository.removeNhanhVnCategoryIdByNhanhCategoryIds(nhanhVnIds);
    }

    @Override
    public List<ProductConfigDTO> mapProductByIds(Set<Long> productIds, Long customerId) {
        List<ProductConfigModel> productConfigModels = productConfigRepository.findAllById(productIds);
        return productConfigConverter.toBasicDTOs(productConfigModels, customerId);
    }

    @Override
    public List<ProductConfigModel> findByIsNewProductTrue() {
        return productConfigRepository.findByIsNewProductTrue();
    }

    @Override
    public List<ProductConfigDTO> findAllByNhanhVnCategoryId(String productCategoryNhanhVnId, Long customerId) {
        List<ProductConfigModel> productConfigModels = productConfigRepository.findByNhanhVnCategoryId(productCategoryNhanhVnId);
        return productConfigConverter.toBasicDTOs(productConfigModels, customerId);
    }

    @Override
    public void addNewProductByIds(List<Long> addIds) {
        productConfigRepository.addNewProductByIds(addIds);
    }

    @Override
    public void removeNewProductByIds(List<Long> removeIds) {
        productConfigRepository.removeNewProductByIds(removeIds);
    }

    @Override
    public Page<ProductConfigDTO> getProductFromNhanhVns(int page, int pageSize, String name, Integer categoryId) throws JSONException {
        JSONObject pagination = new JSONObject();
        pagination.put("page", page + 1);
        pagination.put("icpp", pageSize);
        pagination.put("name", name);
        pagination.put("categoryId", categoryId);
        List<ProductConfigDTO> productConfigDTOFromNhanhVN = new ArrayList<>();

        NhanhVnProductResponse response = nhanhVnAPIService.getListProduct(pagination.toString());
        long totalElement = 0l;
        Pageable pageable = PageRequest.of(page, pageSize);
        if (response.getCode() == 1 && response.getData() != null && response.getData().getProducts() != null
                && !CollectionUtils.isEmpty(response.getData().getProducts().values())) {
            List<NhanhVnProductDataDetailResponse> productFromNhanhVns = new ArrayList<>(response.getData().getProducts().values());

            List<String> nhanhVnIds = productFromNhanhVns.stream().map(NhanhVnProductDataDetailResponse::getIdNhanh).collect(Collectors.toList());

            List<String> nhanhVnIdsFromDb = productConfigRepository.findAllNhanhIdInAndActiveStatus(nhanhVnIds);

            for (NhanhVnProductDataDetailResponse item : productFromNhanhVns) {
                ProductConfigDTO itemDTO = productConfigConverter.convertProductFromNhanhVnToDTO(item);
                itemDTO.setExistInDatabase(nhanhVnIdsFromDb.contains(item.getIdNhanh()));
                productConfigDTOFromNhanhVN.add(itemDTO);
                if (response.getData().getTotalPages() <= response.getData().getCurrentPage()) {
                    pageable = PageRequest.of(response.getData().getTotalPages() - 1, pageSize);
                }
            }
            totalElement = (long) (pageSize * (response.getData().getTotalPages() - 1)) + productConfigDTOFromNhanhVN.size();
        }

        log.info("------------------ Get Products From NhanhVN - END ----------------");
        return new PageImpl<>(productConfigDTOFromNhanhVN, pageable, totalElement);
    }

    @Override
    public ProductConfigDTO getProductDetailFromNhanhVn(String nhanhVnId, Long customerId) {
        NhanhVnProductDetailResponse nhanhVnProductDetail = nhanhVnAPIService.getById(nhanhVnId);
        ProductConfigDTO productConfigDTO = null;

        if (nhanhVnProductDetail != null) {
            ProductConfigModel productConfigModel = findByNhanhVnId(nhanhVnId);
            if (productConfigModel == null) {
                throw new ResourceNotFoundException("Not found product with nhanhVnId: " + nhanhVnId);
            }
            productConfigDTO = productConfigConverter.convertProductFromNhanhVnToDTO(nhanhVnProductDetail, productConfigModel, customerId);
        }

        log.info("------------------ Get Product Detail From NhanhVN by nhanhVnId - END ----------------");
        return productConfigDTO;

    }

    @Override
    public List<ProductConfigModel> findByNhanhVnIdIn(List<String> nhanhVnIds) {
        return productConfigRepository.findByNhanhVnIdIn(nhanhVnIds);
    }

    @Override
    public List<ProductConfigDTO> findAllRelateRroductById(Long id, Long customerId) {
        ProductConfigModel productConfigModel = productConfigRepository.findRelatedNhanhVnProductIdsById(id);
        List<ProductConfigDTO> productConfigDTOs = null;
        if (productConfigModel != null) {
            List<ProductConfigModel> productConfigModels = productConfigRepository.findByNhanhVnIdIn(productConfigModel.getRelatedNhanhVnProductIds());
            productConfigDTOs = productConfigConverter.toBasicDTOs(productConfigModels, customerId);
        }

        log.info("------------------ Get all relate product by id - END ----------------");
        return productConfigDTOs;
    }

    @Override
    public List<ProductConfigDTO> findAllPaidProduct(Long customerId) {
        List<ProductConfigModel> productConfigModels = productConfigRepository.findAllPaidProduct(customerId);
        log.info("------------------ Get all paid product - START ----------------");
        return productConfigConverter.toBasicDTOs(productConfigModels, customerId);
    }

    @Override
    public Map<String, Double> findPriceMapByNhanhVnIdsOrProductCategoryNhanhVnIds(List<String> productNhanhVnIds, List<String> productCategoryNhanhVnIds) {
        Map<String, Double> mapPriceByNhanhVnId = new HashMap<>();
        List<Object[]> results = null;
        if (!CollectionUtils.isEmpty(productNhanhVnIds) && !CollectionUtils.isEmpty(productCategoryNhanhVnIds)) {
            results = productConfigRepository.findIdAndPriceByNhanhVnIdsOrProductCategoryNhanhVnIds(productNhanhVnIds, productCategoryNhanhVnIds);
        } else if (!productNhanhVnIds.isEmpty()) {
            results = productConfigRepository.findIdAndPriceByNhanhVnIds(productNhanhVnIds);
        } else if (!productCategoryNhanhVnIds.isEmpty()) {
            results = productConfigRepository.findIdAndPriceByProductCategoryNhanhVnIds(productCategoryNhanhVnIds);
        }

        if (results != null) {
            for (Object[] result : results) {
                mapPriceByNhanhVnId.putIfAbsent((String) result[0], (Double) result[1]);
            }
        }

        return mapPriceByNhanhVnId;
    }

    @Override
    public void revertPriceAndUpdateSalePriceBackupAllProduct(double salePrice) {
        productConfigRepository.revertPriceAndUpdateSalePriceBackupAllProduct(salePrice);
    }

    @Override
    public void revertPriceAndUpdateSalePriceBackupByIdIn(int salePrice, List<Long> productIds) {
        productConfigRepository.revertPriceAndUpdateSalePriceBackupByIdIn(salePrice, productIds);
    }

    @Override
    public void updatePriceByDiscountAndQuantityAndBackUpOldPriceAllProduct(Double discount, Long quantity) {
        productConfigRepository.updatePriceByDiscountAndQuantityAndBackUpOldPriceAllProduct(discount, quantity);
    }

    @Override
    public void updatePriceByDiscountPercentAndQuantityAndBackUpOldPriceAllProduct(Double discountPercent, Long maxPromotion, Long quantity) {
        if (maxPromotion != null && maxPromotion > 0) {
            productConfigRepository.updatePriceByDiscountPercentAndQuantityAndBackUpOldPriceAllProduct(discountPercent, maxPromotion, quantity);
        } else {
            productConfigRepository.updatePriceByDiscountPercentAndQuantityAndBackUpOldPriceAllProduct(discountPercent, quantity);
        }
    }

    @Override
    public void updatePriceAndSalePriceBackupById(double price, double salePriceBackup, Long id) {
        productConfigRepository.updatePriceAndSalePriceBackupById(price, salePriceBackup, id);
    }

    @Override
    public List<Long> findId() {
        return productConfigRepository.findId();
    }

    @Override
    public List<ProductConfigModel> findProductRelatedByNhanhVnId(String nhanhVnId) {
        return productConfigRepository.findProductRelatedByNhanhVnId(nhanhVnId);
    }

    @Override
    public boolean checkPriceChanged(Map<String, Double> mapByNhanhVnIdAndDisplayPrice) {
        List<ProductConfigModel> productConfigModels = productConfigRepository.findByNhanhVnIdIn(new ArrayList<>(mapByNhanhVnIdAndDisplayPrice.keySet()));
        if (!CollectionUtils.isEmpty(productConfigModels)) {
            for (ProductConfigModel productConfigModel : productConfigModels) {
                Double displayPrice = mapByNhanhVnIdAndDisplayPrice.get(productConfigModel.getNhanhVnId());
                if (displayPrice != null && !displayPrice.equals(productConfigModel.getPrice())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateSaleNewCustomerPriceByDiscountAndQuantityAllProduct(Double discount, Long quantity) {
        productConfigRepository.updateSaleNewCustomerPriceByDiscountAndQuantityAllProduct(discount, quantity);
    }

    @Override
    public void updateSaleNewCustomerPriceByDiscountPercentAndQuantityAllProduct(Double discountPercent, Long maxPromotion, Long quantity) {
        if (maxPromotion != null && maxPromotion > 0) {
            productConfigRepository.updateSaleNewCustomerPriceByDiscountPercentAndQuantityAllProduct(discountPercent, maxPromotion, quantity);
        } else {
            productConfigRepository.updateSaleNewCustomerPriceByDiscountPercentAndQuantityAllProduct(discountPercent, quantity);
        }
    }

    @Override
    public void updateSaleNewCustomerPriceAllProduct(double saleNewCustomerPrice) {
        productConfigRepository.updateSaleNewCustomerPriceAllProduct(saleNewCustomerPrice);
    }

    @Override
    public void updateSaleNewCustomerPriceByIdIn(double saleNewCustomerPrice, List<Long> productIds) {
        productConfigRepository.updateSaleNewCustomerPriceByIdIn(saleNewCustomerPrice, productIds);
    }
}
