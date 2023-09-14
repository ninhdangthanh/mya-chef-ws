package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface OrderDetailRepository extends GenericRepository<OrderDetailModel, Long>, JpaSpecificationExecutor<OrderDetailModel> {
    @Query(value = "SELECT * FROM `order_detail` WHERE order_id = ?1 AND dish_id = ?2", nativeQuery = true)
    List<OrderDetailModel> findByOrderIdAndDishId(Long orderId, Long dishId);
}
