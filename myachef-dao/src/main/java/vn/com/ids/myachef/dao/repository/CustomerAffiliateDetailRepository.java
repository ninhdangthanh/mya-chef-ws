package vn.com.ids.myachef.dao.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.CustomerAffiliateDetailModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface CustomerAffiliateDetailRepository
        extends GenericRepository<CustomerAffiliateDetailModel, Long>, JpaSpecificationExecutor<CustomerAffiliateDetailModel> {

    boolean existsByAffiliateCustomerIdAndReferredCustomerId(Long affiliateCustomerId, Long referredCustomerId);

    @Query(value = "SELECT ca.referredCustomerId FROM CustomerAffiliateDetailModel ca")
    List<Long> getUsedIds();

    boolean existsByAffiliateCustomerId(Long id);

    @Query(value = "SELECT `referred_customer_id`, COUNT(`referred_customer_id`) AS `count` " //
            + "FROM `customer_affiliate_detail` " //
            + "WHERE DATE(`created_date`) >= ?1 " //
            + "AND DATE(`created_date`) <= ?2 " //
            + "GROUP BY `referred_customer_id` " //
            + "ORDER BY `count` DESC " //
            + "LIMIT ?3,?4", nativeQuery = true)
    List<Object[]> countByReferredCustomerId(LocalDate fromDate, LocalDate toDate, int start, int pageSize);

    @Query(value = "SELECT COUNT(DISTINCT(`referred_customer_id`)) " //
            + "FROM `customer_affiliate_detail` " //
            + "WHERE DATE(`created_date`) >= ?1 " //
            + "AND DATE(`created_date`) <= ?2 ", nativeQuery = true)
    Long countByReferred(LocalDate fromDate, LocalDate toDate);

    List<CustomerAffiliateDetailModel> findByReferredCustomerId(Long referredCustomerId);

    List<CustomerAffiliateDetailModel> findByAffiliateCustomerId(Long affiliateCustomerId);
}
