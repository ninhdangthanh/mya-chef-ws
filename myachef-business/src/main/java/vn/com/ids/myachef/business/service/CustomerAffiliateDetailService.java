package vn.com.ids.myachef.business.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.CustomerAffiliateDetailDTO;
import vn.com.ids.myachef.business.dto.CustomerDTO;
import vn.com.ids.myachef.dao.criteria.CustomerAffiliateDetailCriteria;
import vn.com.ids.myachef.dao.model.CustomerAffiliateDetailModel;

public interface CustomerAffiliateDetailService extends IGenericService<CustomerAffiliateDetailModel, Long> {

    boolean existsByAffiliateCustomerIdAndReferredCustomerId(Long affiliateCustomerId, Long referredCustomerId);

    Page<CustomerAffiliateDetailDTO> search(CustomerAffiliateDetailCriteria customerAffiliateDetailCriteria);

    boolean existsByAffiliateCustomerId(Long id);

    Page<CustomerDTO> findReferredCustomerByPeriodTime(LocalDate fromDate, LocalDate toDate, int pageIndex, int pageSize);

    List<CustomerAffiliateDetailModel> findByReferredCustomerId(Long referredCustomerId);

    List<CustomerAffiliateDetailModel> findByAffiliateCustomerId(Long affiliateCustomerId);
}
