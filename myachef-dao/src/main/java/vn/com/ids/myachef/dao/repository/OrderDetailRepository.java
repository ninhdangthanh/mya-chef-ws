package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface OrderDetailRepository extends GenericRepository<OrderDetailModel, Long>, JpaSpecificationExecutor<OrderDetailModel> {    

}
