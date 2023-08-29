package vn.com.ids.myachef.dao.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.SaleModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface SaleRepository extends GenericRepository<SaleModel, Long>, JpaSpecificationExecutor<SaleModel> {

    @Query(value = "SELECT s FROM SaleModel s WHERE s.status = 'ACTIVE' AND s.type = 'FLASH_SALE' AND s.startDate <= NOW() AND s.endDate >= NOW() AND s.isIntroduceCustomer = false")
    List<SaleModel> getAllFlashSaleActive();

    @Query(value = "SELECT s FROM SaleModel s " //
            + "WHERE s.status = 'ACTIVE' " //
            + "AND s.type = 'VOUCHER' " //
            + "AND s.startDate <= NOW() " //
            + "AND s.endDate >= NOW() " //
            + "AND s.totalQuantity > s.totalQuantityUsed " //
            + "AND s.isIntroduceCustomer = false")
    List<SaleModel> findVoucherActive();

    List<SaleModel> findByCode(String code);

    @Query(value = "UPDATE `sale` SET `status` = 'IN_ACTIVE' WHERE `is_introduce_customer` = true AND `status` = 'ACTIVE' AND `id` != ?1", nativeQuery = true)
    @Modifying
    void disableAllAnotherIntroduceCustomer(Long saleId);

    @Query(value = "UPDATE `sale` SET `status` = 'IN_ACTIVE' WHERE `is_introduce_customer` = true AND `status` = 'ACTIVE' AND `introduce_customer_scope` IN (?1) AND `id` != ?2", nativeQuery = true)
    @Modifying
    void disableAllAnotherIntroduceCustomerByScopeIn(List<String> scopes, Long saleId);

    @Query(value = "SELECT s FROM SaleModel s WHERE s.status = 'ACTIVE' AND s.startDate <= NOW() AND s.endDate >= NOW() AND s.isIntroduceCustomer = true")
    List<SaleModel> findIntroduceCustomerSaleCampaign();

    @Query(value = "SELECT s FROM SaleModel s WHERE s.id IN(SELECT g.sale.id FROM GiftModel g WHERE g.customer.id = ?1 AND g.expiredDate >= NOW())")
    List<SaleModel> findGiftByCustomerId(Long customerId);

    @Query(value = "SELECT * FROM `sale` WHERE `status` = 'ACTIVE' AND `start_date` <= NOW() AND `end_date` >= NOW() AND `is_introduce_customer` = true AND `introduce_customer_scope` IN (?1) LIMIT 1", nativeQuery = true)
    SaleModel findIntroduceCustomerSaleCampaignByScopeIn(List<String> scopes);

    @Query(value = "SELECT IF(COUNT(s.`id`) > 0, 'true', 'false') FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND (s.`type` = 'FLASH_SALE' OR s.`type` = 'SALE_CAMPAIGN') " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1", nativeQuery = true)
    boolean isExistByTimeFrame(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT IF(COUNT(s.`id`) > 0, 'true', 'false') FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND (s.`type` = 'FLASH_SALE' OR s.`type` = 'SALE_CAMPAIGN') " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1 " //
            + "AND (s.`sale_scope` = 'ALL' OR EXISTS " //
            + "(SELECT `id` FROM `sale_detail` sd " //
            + "WHERE s.`id` = sd.`sale_id` " //
            + "AND sd.`product_id` IN ?3))", nativeQuery = true)
    boolean isExistByTimeFrameAndProductIds(LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds);

    @Query(value = "SELECT `product_id` FROM `sale_detail` sd WHERE sd.`sale_id` IN " //
            + "(SELECT s.`id` FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND (s.`type` = 'FLASH_SALE' OR s.`type` = 'SALE_CAMPAIGN') " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1)", nativeQuery = true)
    List<Long> findProductIdsExistByTimeFrame(LocalDateTime fromDate, LocalDateTime toDate);

    @Query(value = "SELECT * FROM `sale` " //
            + "WHERE `status` = 'ACTIVE' " //
            + "AND (`type` = 'FLASH_SALE' OR `type` = 'SALE_CAMPAIGN' OR `type` = 'SALE_FOR_NEW_CUSTOMER') " //
            + "AND `is_introduce_customer` = FALSE " //
            + "AND `promotion_type` = 'SALE' " //
            + "AND `is_update_price` = TRUE " //
            + "AND `start_date` <= NOW() ", nativeQuery = true)
    List<SaleModel> findSaleStartByTimeFrame();

    @Query(value = "SELECT * FROM `sale` " //
            + "WHERE `status` = 'ACTIVE' " //
            + "AND (`type` = 'FLASH_SALE' OR `type` = 'SALE_CAMPAIGN' OR `type` = 'SALE_FOR_NEW_CUSTOMER') " //
            + "AND `is_introduce_customer` = FALSE " //
            + "AND `promotion_type` = 'SALE' " //
            + "AND `end_date` <= NOW() " //
            + "AND `is_revert_price` = TRUE ", nativeQuery = true)
    List<SaleModel> findSaleEndByTimeFrame();

    @Query(value = "SELECT `product_id` FROM `sale_detail` sd WHERE sd.`sale_id` IN " //
            + "(SELECT s.`id` FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND (s.`type` = 'FLASH_SALE' OR s.`type` = 'SALE_CAMPAIGN') " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1 " //
            + "AND s.`id` != ?3)", nativeQuery = true)
    List<Long> findProductIdsExistByTimeFrameIgnoreBySaleId(LocalDateTime fromDate, LocalDateTime toDate, Long saleId);

    @Query(value = "SELECT IF(COUNT(s.`id`) > 0, 'true', 'false') FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND (s.`type` = 'FLASH_SALE' OR s.`type` = 'SALE_CAMPAIGN') " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND `sale_scope` = 'ALL' " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1", nativeQuery = true)
    boolean existByTimeFrameAndScopeAll(LocalDateTime fromDate, LocalDateTime toDate);

    @Query(value = "SELECT IF(COUNT(s.`id`) > 0, 'true', 'false') FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND (s.`type` = 'FLASH_SALE' OR s.`type` = 'SALE_CAMPAIGN') " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND `sale_scope` = 'ALL' " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1 " //
            + "AND s.`id` != ?3", nativeQuery = true)
    boolean existByTimeFrameAndScopeAllIgnoreSaleId(LocalDateTime fromDate, LocalDateTime toDate, Long saleId);

    @Query(value = "SELECT IF(COUNT(s.`id`) > 0, 'true', 'false') FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND (s.`type` = 'FLASH_SALE' OR s.`type` = 'SALE_CAMPAIGN') " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1 " //
            + "AND s.`id` != ?3", nativeQuery = true)
    boolean isExistByTimeFrameIgnoreId(LocalDateTime startDate, LocalDateTime endDate, Long id);

    @Query(value = "SELECT IF(COUNT(s.`id`) > 0, 'true', 'false') FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND (s.`type` = 'FLASH_SALE' OR s.`type` = 'SALE_CAMPAIGN') " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1 " //
            + "AND s.`id` != ?4 " //
            + "AND (s.`sale_scope` = 'ALL' OR EXISTS " //
            + "(SELECT `id` FROM `sale_detail` sd " //
            + "WHERE s.`id` = sd.`sale_id` " //
            + "AND sd.`product_id` IN ?3))", nativeQuery = true)
    boolean isExistByTimeFrameAndProductIdsIgnoreId(LocalDateTime startDate, LocalDateTime endDate, List<Long> productIds, Long id);

    @Query(value = "SELECT * FROM `sale` " //
            + "WHERE `status` = 'ACTIVE' " //
            + "AND (`type` = 'FLASH_SALE' OR `type` = 'SALE_CAMPAIGN') " //
            + "AND `is_introduce_customer` = FALSE " //
            + "AND `promotion_type` = 'SALE' " //
            + "AND `sale_scope` = 'ALL' " //
            + "AND `end_date` >= NOW() " //
            + "AND `start_date` <= NOW() ", nativeQuery = true)
    List<SaleModel> findSaleAllInProgress();

    @Query(value = "SELECT IF(COUNT(s.`id`) > 0, 'true', 'false') FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND s.`type` = 'SALE_FOR_NEW_CUSTOMER' " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1 ", nativeQuery = true)
    boolean existSaleForCustomerActiveInTimeFrame(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT IF(COUNT(s.`id`) > 0, 'true', 'false') FROM `sale` s " //
            + "WHERE s.`status` = 'ACTIVE' " //
            + "AND s.`type` = 'SALE_FOR_NEW_CUSTOMER' " //
            + "AND s.`is_introduce_customer` = FALSE " //
            + "AND s.`start_date` <= ?2 " //
            + "AND s.`end_date` >= ?1 " //
            + "AND s.`id` != ?3", nativeQuery = true)
    boolean existSaleForCustomerActiveInTimeFrameIgnoreId(LocalDateTime startDate, LocalDateTime endDate, Long saleId);

    @Query(value = "SELECT * FROM `sale` " //
            + "WHERE `status` = 'ACTIVE' " //
            + "AND (`type` = 'FLASH_SALE' OR `type` = 'SALE_CAMPAIGN') " //
            + "AND `is_introduce_customer` = FALSE " //
            + "AND `promotion_type` = 'SALE' " //
            + "AND `end_date` >= NOW() " //
            + "AND `start_date` <= NOW() " //
            + "AND (`sale_scope` = 'ALL' OR `id` IN (SELECT `sale_id` FROM `sale_detail` WHERE `product_id` = ?1))", nativeQuery = true)
    List<SaleModel> findSaleActiveScopeAllOrByProductId(Long productId);
}
