package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.SubscriptionModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface SubscriptionRepository extends GenericRepository<SubscriptionModel, Long>, JpaSpecificationExecutor<SubscriptionModel> {

    @Query(value = "SELECT * FROM `subscription` " //
            + "WHERE `id` IN (SELECT `subscription_id` " //
            + "FROM `subscription_customer_detail` " //
            + "WHERE `payment_status` = 'PAID' " //
            + "AND `expired_date` < NOW() " //
            + "AND `customer_id` = ?1)", nativeQuery = true)
    List<SubscriptionModel> findPaid(Long customerId);

    @Query(value = "SELECT * FROM `subscription` WHERE `id` IN (SELECT `subscription_id` FROM `subscription_customer_detail` WHERE `payment_status` = 'PAID' AND `type` = 'BUY' AND `customer_id` = ?1)", nativeQuery = true)
    List<SubscriptionModel> findYourSubscription(Long customerId);

    List<SubscriptionModel> findByStatus(Status status);

}
