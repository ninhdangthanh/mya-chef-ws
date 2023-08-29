package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.SaleDetailModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface SaleDetailRepository extends GenericRepository<SaleDetailModel, Long>, JpaSpecificationExecutor<SaleDetailModel> {

    @Query(value = "SELECT `sale_id` FROM `sale_detail` WHERE `product_id` IN (SELECT `id` FROM `product_config` WHERE `nhanh_vn_id` IN (?1))", nativeQuery = true)
    List<Long> findSaleIdByNhanhByNhanhVnProductIdIn(List<String> nhanhVnProductIds);

    @Query(value = "SELECT `product_id` FROM `sale_detail` WHERE `sale_id` = ?1", nativeQuery = true)
    List<Long> findProductIdBySaleId(Long saleId);

    @Query(value = "SELECT * FROM `sale_detail` " //
            + "WHERE `product_id` = ?1 " //
            + "AND `sale_id` IN (SELECT `id` FROM `sale` " //
            + "WHERE `status` = 'ACTIVE' " //
            + "AND (`type` = 'FLASH_SALE' OR `type` = 'SALE_CAMPAIGN') " //
            + "AND `is_introduce_customer` = FALSE " //
            + "AND `promotion_type` = 'SALE' " //
            + "AND `start_date` <= NOW() " //
            + "AND `end_date` >= NOW())", nativeQuery = true)
    List<SaleDetailModel> findByProductId(Long id);

}
