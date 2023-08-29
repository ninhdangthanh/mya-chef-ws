package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.model.CustomerAffiliateDetailModel;

public class CustomerAffiliateDetailBuilder {
    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<CustomerAffiliateDetailModel> root;
    private final CriteriaBuilder builder;

    public CustomerAffiliateDetailBuilder(Root<CustomerAffiliateDetailModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public CustomerAffiliateDetailBuilder setAffiliateCustomerId(Long affiliateCustomerId) {
        if (affiliateCustomerId != null) {
            predicates.add(builder.equal(root.get("affiliateCustomerId"), affiliateCustomerId));
        }
        return this;
    }

    public CustomerAffiliateDetailBuilder setReferredCustomerId(Long referredCustomerId) {
        if (referredCustomerId != null) {
            predicates.add(builder.equal(root.get("referredCustomerId"), referredCustomerId));
        }
        return this;
    }

    public CustomerAffiliateDetailBuilder setAffiliateCustomerFullName(String affiliateCustomerFullName) {
        if (StringUtils.hasText(affiliateCustomerFullName)) {
            predicates.add(builder.like(root.get("affiliateCustomerFullName"), "%" + affiliateCustomerFullName.trim() + "%"));
        }
        return this;
    }

    public CustomerAffiliateDetailBuilder setReferredCustomerFullName(String referredCustomerFullName) {
        if (StringUtils.hasText(referredCustomerFullName)) {
            predicates.add(builder.like(root.get("searchText"), "%" + referredCustomerFullName.trim() + "%"));
        }
        return this;
    }
}
