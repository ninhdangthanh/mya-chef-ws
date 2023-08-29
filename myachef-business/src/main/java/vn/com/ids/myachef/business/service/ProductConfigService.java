package vn.com.ids.myachef.business.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.json.JSONException;
import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.ProductConfigDTO;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnWebHooksData;
import vn.com.ids.myachef.dao.criteria.ProductConfigCriteria;
import vn.com.ids.myachef.dao.model.ProductConfigModel;

public interface ProductConfigService extends IGenericService<ProductConfigModel, Long> {

    Page<ProductConfigModel> search(ProductConfigCriteria productConfigCriteria);

    List<ProductConfigModel> create(@Valid List<ProductConfigDTO> productConfigDTOs);

    void updateProductInfoFromNhanhVn(NhanhVnWebHooksData data);

    ProductConfigModel findByNhanhVnId(String nhanhVnProductId);

    void removeNhanhVnCategoryIdByNhanhCategoryIds(List<String> nhanhVnIds);

    List<ProductConfigDTO> mapProductByIds(Set<Long> productIds, Long customerId);

    List<ProductConfigModel> findByIsNewProductTrue();

    List<ProductConfigDTO> findAllByNhanhVnCategoryId(String productCategoryNhanhVnId, Long customerId);

    void addNewProductByIds(List<Long> addIds);

    void removeNewProductByIds(List<Long> removeIds);

    Page<ProductConfigDTO> getProductFromNhanhVns(int page, int pageSize, String name, Integer categoryId) throws JSONException;

    ProductConfigDTO getProductDetailFromNhanhVn(String nhanhVnId, Long customerId);

    List<ProductConfigModel> findByNhanhVnIdIn(List<String> nhanhVnIds);

    List<ProductConfigDTO> findAllRelateRroductById(Long id, Long customerId);

    List<ProductConfigDTO> findAllPaidProduct(Long customerId);

    Map<String, Double> findPriceMapByNhanhVnIdsOrProductCategoryNhanhVnIds(List<String> productNhanhVnIds, List<String> productCategoryNhanhVnIds);

    void revertPriceAndUpdateSalePriceBackupAllProduct(double salePrice);

    void revertPriceAndUpdateSalePriceBackupByIdIn(int salePrice, List<Long> productIds);

    void updatePriceByDiscountAndQuantityAndBackUpOldPriceAllProduct(Double discount, Long quantity);

    void updatePriceByDiscountPercentAndQuantityAndBackUpOldPriceAllProduct(Double discountPercent, Long maxPromotion, Long quantity);

    void updatePriceAndSalePriceBackupById(double price, double salePriceBackup, Long id);

    List<Long> findId();

    List<ProductConfigModel> findProductRelatedByNhanhVnId(String nhanhVnId);

    boolean checkPriceChanged(Map<String, Double> mapByNhanhVnIdAndDisplayPrice);

    void updateSaleNewCustomerPriceByDiscountAndQuantityAllProduct(Double discount, Long quantity);

    void updateSaleNewCustomerPriceByDiscountPercentAndQuantityAllProduct(Double discountPercent, Long maxPromotion, Long quantity);

    void updateSaleNewCustomerPriceAllProduct(double saleNewCustomerPrice);

    void updateSaleNewCustomerPriceByIdIn(double saleNewCustomerPrice, List<Long> productIds);
}
