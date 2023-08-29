package vn.com.ids.myachef.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.CustomerDTO;
import vn.com.ids.myachef.business.payload.request.ProfileRequest;
import vn.com.ids.myachef.business.zalo.social.ZaloUser;
import vn.com.ids.myachef.dao.criteria.CustomerCriteria;
import vn.com.ids.myachef.dao.model.CustomerModel;

public interface CustomerService extends IGenericService<CustomerModel, Long> {

    public Page<CustomerModel> findAll(CustomerCriteria customerCriteria);

    public CustomerModel findByUsername(String username);

    public CustomerModel register(ZaloUser zaloUserInfo);

    public CustomerDTO updateByZaloProfile(ProfileRequest profileRequest, Long authenticatedUserId);

    public CustomerDTO update(CustomerDTO customerDTO);

    public void useAffiliateCode(String affiliateCode, Long customerId);

    public Map<String, Integer> findTotalYourOrderStatus(Long authenticatedUserId);

    public void createGitfForReferredCustomer(Long affiliateCustomerId);

    public List<CustomerDTO> FindCustomerByReferredCustomerId(Long referredCustomerId);

    public Boolean isNewCustomer(Long customerId);

    public List<String> findCustomerAffiliateAvatarByReferredCustomerId(Long referredCustomerId);
}
