package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface OrderRepository extends GenericRepository<OrderModel, Long>, JpaSpecificationExecutor<OrderModel> {
    @Query(value = "SELECT * FROM `order` WHERE dinner_table_id = ?1 AND `status` =  'UNPAID' LIMIT 1", nativeQuery = true)
    OrderModel findOrderExistingByDinnerTableId(Long dinnerTableId);
}
