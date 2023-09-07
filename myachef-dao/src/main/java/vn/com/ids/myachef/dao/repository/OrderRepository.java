package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface OrderRepository extends GenericRepository<OrderModel, Long>, JpaSpecificationExecutor<OrderModel> {

}
