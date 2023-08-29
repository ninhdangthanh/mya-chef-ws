package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.SubscriptionDetailModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface SubscriptionDetailRepository extends GenericRepository<SubscriptionDetailModel, Long>, JpaSpecificationExecutor<SubscriptionDetailModel> {

    @Query(value = "SELECT * FROM `subscription_detail` WHERE `id` IN (SELECT `subscription_detail_id` FROM `subscription_customer_detail` WHERE `id` IN ?1)", nativeQuery = true)
    List<SubscriptionDetailModel> findBySubscriptionCustomerDetailIds(List<Long> subscriptionCustomerDetailIds);

}
