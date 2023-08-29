package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.enums.CustomerStatus;
import vn.com.ids.myachef.dao.model.CustomerModel;

public class CustomerSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<CustomerModel> root;
    private final CriteriaBuilder builder;

    public CustomerSpecificationBuilder(Root<CustomerModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public CustomerSpecificationBuilder setSearchText(String searchText) {
        if (StringUtils.hasText(searchText)) {
            predicates.add(builder.like(root.get("searchText"), "%" + searchText + "%"));
        }
        return this;
    }

    public CustomerSpecificationBuilder setStatus(CustomerStatus status) {
        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        } else {
            predicates.add(root.get("status").in(CustomerStatus.ACTIVE, CustomerStatus.IN_ACTIVE));
        }
        return this;
    }

    public CustomerSpecificationBuilder setReferrerAffiliateCode(String referrerAffiliateCode) {
        if (referrerAffiliateCode != null) {
            predicates.add(builder.equal(root.get("referrerAffiliateCode"), referrerAffiliateCode));
        }
        return this;
    }

}
