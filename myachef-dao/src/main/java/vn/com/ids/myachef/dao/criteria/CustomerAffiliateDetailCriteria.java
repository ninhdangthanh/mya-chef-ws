package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerAffiliateDetailCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    private Long affiliateCustomerId;

    private String affiliateCustomerFullName;

    private Long referredCustomerId;

    private String referredCustomerFullName;
}
