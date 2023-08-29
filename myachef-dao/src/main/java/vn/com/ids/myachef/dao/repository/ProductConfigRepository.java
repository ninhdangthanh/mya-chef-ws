package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.ProductConfigModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface ProductConfigRepository extends GenericRepository<ProductConfigModel, Long>, JpaSpecificationExecutor<ProductConfigModel> {

    ProductConfigModel findByNhanhVnId(String productId);

    @Query(value = "UPDATE `product_config` SET `nhanh_vn_category_id` = null WHERE `nhanh_vn_category_id` IN ?1", nativeQuery = true)
    @Modifying
    void removeNhanhVnCategoryIdByNhanhCategoryIds(List<String> nhanhVnIds);

    List<ProductConfigModel> findByIsNewProductTrue();

    List<ProductConfigModel> findByNhanhVnCategoryId(String productCategoryNhanhVnId);

    @Query(value = "UPDATE `product_config` SET `is_new_product` = true WHERE `id` IN ?1", nativeQuery = true)
    @Modifying
    void addNewProductByIds(List<Long> addIds);

    @Query(value = "UPDATE `product_config` SET `is_new_product` = false WHERE `id` IN ?1", nativeQuery = true)
    @Modifying
    void removeNewProductByIds(List<Long> removeIds);

    @Query(value = "DELETE FROM `product_config` WHERE `nhanh_vn_id` IN ?1", nativeQuery = true)
    @Modifying
    void deleteByIdIn(List<String> ids);

    @Query(value = "SELECT p.nhanhVnId FROM ProductConfigModel p WHERE p.nhanhVnId IN ?1 AND p.status = 'ACTIVE'")
    List<String> findAllNhanhIdInAndActiveStatus(List<String> nhanhVnIds);

    List<ProductConfigModel> findByNhanhVnIdIn(List<String> nhanhVnIds);

    ProductConfigModel findRelatedNhanhVnProductIdsByNhanhVnId(String nhanhVnId);

    ProductConfigModel findRelatedNhanhVnProductIdsById(Long id);

    @Query(value = "SELECT * FROM `product_config` WHERE `id` IN (SELECT `product_id` FROM `order_detail` WHERE `order_id` IN (SELECT id FROM `order` WHERE `customer_id` = ?1))", nativeQuery = true)
    List<ProductConfigModel> findAllPaidProduct(Long customerId);

    @Query(value = "SELECT `nhanh_vn_id`, `price` FROM `product_config` WHERE `nhanh_vn_id` IN ?1 OR `nhanh_vn_category_id` IN ?2", nativeQuery = true)
    List<Object[]> findIdAndPriceByNhanhVnIdsOrProductCategoryNhanhVnIds(List<String> productNhanhVnIds, List<String> productCategoryNhanhVnIds);

    @Query(value = "SELECT `nhanh_vn_id`, `price` FROM `product_config` WHERE `nhanh_vn_id` IN ?1", nativeQuery = true)
    List<Object[]> findIdAndPriceByNhanhVnIds(List<String> productNhanhVnIds);

    @Query(value = "SELECT `nhanh_vn_id`, `price` FROM `product_config` WHERE `nhanh_vn_category_id` IN ?1", nativeQuery = true)
    List<Object[]> findIdAndPriceByProductCategoryNhanhVnIds(List<String> productCategoryNhanhVnIds);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `price` = IF(`sale_price_backup` != -1, `sale_price_backup`, `price`), `sale_price_backup` = ?1", nativeQuery = true)
    void revertPriceAndUpdateSalePriceBackupAllProduct(double salePrice);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `price` = IF(`sale_price_backup` != -1, `sale_price_backup`, `price`), `sale_price_backup` = ?1 WHERE `id` IN ?2", nativeQuery = true)
    void revertPriceAndUpdateSalePriceBackupByIdIn(double salePrice, List<Long> productIds);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `sale_quantity` = ?2, `sale_price_backup` = `price`, `price` = IF(`price` - ?1 < 0, 0, `price` - ?1)", nativeQuery = true)
    void updatePriceByDiscountAndQuantityAndBackUpOldPriceAllProduct(Double discount, Long quantity);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `sale_quantity` = ?2, `sale_price_backup` = `price`, `price` = IF(`price` - (`price` * ?1 / 100) < 0, 0, `price` - (`price` * ?1 / 100))", nativeQuery = true)
    void updatePriceByDiscountPercentAndQuantityAndBackUpOldPriceAllProduct(Double discountPercent, Long quantity);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `sale_quantity` = ?3, `sale_price_backup` = `price`, `price` = IF((`price` * ?1 / 100) > ?2, `price` - ?2, IF(`price` - (`price` * ?1 / 100) < 0, 0, `price` - (`price` * ?1 / 100)))", nativeQuery = true)
    void updatePriceByDiscountPercentAndQuantityAndBackUpOldPriceAllProduct(Double discountPercent, Long maxPromotion, Long quantity);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `price` = ?1, `sale_price_backup` = ?2 WHERE `id` = ?3", nativeQuery = true)
    void updatePriceAndSalePriceBackupById(double price, double salePriceBackup, Long id);

    @Query(value = "SELECT `id` FROM `product_config`", nativeQuery = true)
    List<Long> findId();

    @Query(value = "SELECT * FROM `product_config` WHERE JSON_CONTAINS(`related_nhanh_vn_product_ids`, JSON_QUOTE(?1),'$')", nativeQuery = true)
    List<ProductConfigModel> findProductRelatedByNhanhVnId(String nhanhVnId);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `sale_new_customer_quantity` = ?2, `sale_new_customer_price` = IF(`sale_price_backup` != -1, IF(`sale_price_backup` - ?1 < 0, 0, `sale_price_backup` - ?1), IF(`price` - ?1 < 0, 0, `price` - ?1))", nativeQuery = true)
    void updateSaleNewCustomerPriceByDiscountAndQuantityAllProduct(Double discount, Long quantity);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `sale_new_customer_quantity` = ?3, `sale_new_customer_price` = IF(`sale_price_backup` != -1, " //
            + "IF((`sale_price_backup` * ?1 / 100) > ?2, `sale_price_backup` - ?2, IF(`sale_price_backup` - (`sale_price_backup` * ?1 / 100) < 0, 0, `sale_price_backup` - (`sale_price_backup` * ?1 / 100))), " //
            + "IF((`price` * ?1 / 100) > ?2, `price` - ?2, IF(`price` - (`price` * ?1 / 100) < 0, 0, `price` - (`price` * ?1 / 100))))", nativeQuery = true)
    void updateSaleNewCustomerPriceByDiscountPercentAndQuantityAllProduct(Double discountPercent, Long maxPromotion, Long quantity);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `sale_new_customer_quantity` = ?2, `sale_new_customer_price` = IF(`sale_price_backup` != -1, " //
            + "IF(`sale_price_backup` - (`sale_price_backup` * ?1 / 100) < 0, 0, `sale_price_backup` - (`sale_price_backup` * ?1 / 100)), " //
            + "IF(`price` - (`price` * ?1 / 100) < 0, 0, `price` - (`price` * ?1 / 100)))", nativeQuery = true)
    void updateSaleNewCustomerPriceByDiscountPercentAndQuantityAllProduct(Double discountPercent, Long quantity);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `sale_new_customer_price` = ?1", nativeQuery = true)
    void updateSaleNewCustomerPriceAllProduct(double saleNewCustomerPrice);

    @Modifying
    @Query(value = "UPDATE `product_config` SET `sale_new_customer_price` = ?1 WHERE `id` IN ?2", nativeQuery = true)
    void updateSaleNewCustomerPriceByIdIn(double saleNewCustomerPrice, List<Long> productIds);
}
