package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface CustomerRepository extends GenericRepository<CustomerModel, Long>, JpaSpecificationExecutor<CustomerModel> {

    public CustomerModel findByUsername(String username);

    public CustomerModel findByAffiliateCode(String affiliateCode);

    public List<CustomerModel> findByIdIn(List<Long> ids);

    @Query(value = "SELECT c FROM CustomerModel c WHERE c.id IN (SELECT ca.referredCustomerId FROM CustomerAffiliateDetailModel ca WHERE ca.affiliateCustomerId = ?1)")
    public List<CustomerModel> findCustomerByAffiliateCustomerId(Long affiliateCustomerId);

    @Query(value = "SELECT * FROM `customers` WHERE `id` IN (SELECT `affiliate_customer_id` FROM `customer_affiliate_detail` WHERE `referred_customer_id` = ?1)", nativeQuery = true)
    public List<CustomerModel> FindCustomerByReferredCustomerId(Long referredCustomerId);

    @Query(value = "SELECT if(COUNT(`id`) > 0, 'false', 'true') FROM `order` WHERE `customer_id` = ?1", nativeQuery = true)
    public Boolean isNewCustomer(Long customerId);

    @Query(value = "SELECT `avatar` FROM `customers` WHERE `id` IN (SELECT `affiliate_customer_id` FROM `customer_affiliate_detail` WHERE = `referred_customer_id` = ?1", nativeQuery = true)
    public List<String> findCustomerAffiliateAvatarByReferredCustomerId(Long referredCustomerId);

}
