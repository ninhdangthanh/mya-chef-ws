package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.CustomerStatus;

@Getter
@Setter
public class CustomerCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    private String referrerAffiliateCode;

    private String searchText;

    private CustomerStatus status;

}
