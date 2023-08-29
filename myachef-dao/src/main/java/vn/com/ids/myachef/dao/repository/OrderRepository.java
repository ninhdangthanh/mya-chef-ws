package vn.com.ids.myachef.dao.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface OrderRepository extends GenericRepository<OrderModel, Long>, JpaSpecificationExecutor<OrderModel> {

    @Modifying
    @Query(value = "UPDATE OrderModel o SET o.status = ?2 WHERE o.id = ?1 AND status IN ('NEW', 'CONFIRMING', 'CONFIRMED')")
    int updateStatusByNhanhVnId(String nhanhVnId, OrderStatus canceled);

    OrderModel findByNhanhVnId(String nhanhVnId);

    Integer countByCustomerIdAndStatusIn(Long customerId, List<OrderStatus> status);

    @Query(value = "SELECT if(COUNT(`id`) > 0, 'true', 'false') FROM `order` WHERE `customer_id` = ?1", nativeQuery = true)
    boolean existsByCustomerId(Long customerId);

    @Query(value = "SELECT SUM(od.`quantity`) FROM `order` o, `order_detail` od " //
            + "WHERE o.`created_date` >= ?1 " //
            + "AND o.`created_date` <= ?2 " //
            + "AND o.`customer_id` = ?3 " //
            + "AND o.`id` = od.`order_id` " //
            + "AND od.`product_id` = ?4", nativeQuery = true)
    Integer findQuantityProductSaleOrdered(LocalDateTime startDate, LocalDateTime endDate, Long customerId, Long productId);

}
