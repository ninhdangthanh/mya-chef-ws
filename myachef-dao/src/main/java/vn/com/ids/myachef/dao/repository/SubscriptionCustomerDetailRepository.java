package vn.com.ids.myachef.dao.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.enums.PaymentStatus;
import vn.com.ids.myachef.dao.model.SubscriptionCustomerDetailModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface SubscriptionCustomerDetailRepository
        extends GenericRepository<SubscriptionCustomerDetailModel, Long>, JpaSpecificationExecutor<SubscriptionCustomerDetailModel> {

    @Query(value = "SELECT IF((SELECT COUNT(*) FROM `subscription_customer_detail` " //
            + "WHERE `payment_status` = 'PAID' " //
            + "AND DATE(NOW()) < `expired_date` " //
            + "AND `customer_id` = ?1) > 0, 'true', 'false')", nativeQuery = true)
    boolean existsSubscriptionByCustomerId(Long customerId);

    SubscriptionCustomerDetailModel findByPaymentStatusNotAndCustomerId(PaymentStatus paid, Long customerId);

    @Query(value = "SELECT s FROM SubscriptionCustomerDetailModel s WHERE s.type = 'GIFT' AND DATE(NOW()) < expiredDate AND s.customer.id = ?1")
    List<SubscriptionCustomerDetailModel> findYourGift(Long customerId);

    @Query(value = "SELECT c FROM SubscriptionDetailModel d, SubscriptionCustomerDetailModel c " //
            + "WHERE c.type = 'GIFT' " //
            + "AND DATE(NOW()) < c.expiredDate " //
            + "AND c.customer.id = ?2 " //
            + "AND d.id = c.subscriptionDetail.id " //
            + "AND d.minimumOrderPrice <= ?1")
    List<SubscriptionCustomerDetailModel> findGiftCanUse(Double totalAmount, Long customerId);

    @Query(value = "SELECT if(COUNT(`id`) > 0, 'true', 'false') FROM `subscription_customer_detail` WHERE `payment_status` = 'PAID' AND `subscription_id` = ?1", nativeQuery = true)
    boolean existPaidBySubscriptionId(Long subscriptionId);

    @Query(value = "SELECT `expired_date` FROM `subscription_customer_detail` WHERE `payment_status` = 'PAID' AND `subscription_id` = ?1 AND `customer_id` = ?2", nativeQuery = true)
    LocalDate findExpiredDateBySubscriptionIdAndCustomerId(Long subscriptionId, Long customerId);

}
