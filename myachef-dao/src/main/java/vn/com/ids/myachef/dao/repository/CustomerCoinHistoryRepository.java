package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.CustomerCoinHistoryModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface CustomerCoinHistoryRepository extends GenericRepository<CustomerCoinHistoryModel, Long>, JpaSpecificationExecutor<CustomerCoinHistoryModel> {

}
