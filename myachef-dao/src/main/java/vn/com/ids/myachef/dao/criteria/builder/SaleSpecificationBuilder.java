package vn.com.ids.myachef.dao.criteria.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.enums.IntroduceCustomerScope;
import vn.com.ids.myachef.dao.enums.PromotionType;
import vn.com.ids.myachef.dao.enums.SaleScope;
import vn.com.ids.myachef.dao.enums.SaleType;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.SaleModel;

public class SaleSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<SaleModel> root;
    private final CriteriaBuilder builder;

    public SaleSpecificationBuilder(Root<SaleModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public SaleSpecificationBuilder setName(String name) {
        if (StringUtils.hasText(name)) {
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public SaleSpecificationBuilder setCode(String code) {
        if (code != null) {
            predicates.add(builder.equal(root.get("code"), code));
        }
        return this;
    }

    public SaleSpecificationBuilder setStatus(Status status) {
        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        }
        return this;
    }

    public SaleSpecificationBuilder setTimeFrame(LocalDateTime from, LocalDateTime to) {
        if (from != null) {
            predicates.add(builder.between(root.get("createdDate"), from, to));
        }
        if (to != null) {
            predicates.add(builder.between(root.get("createdDate"), from, to));
        }

        return this;
    }

    public SaleSpecificationBuilder setType(SaleType type) {
        if (type != null) {
            predicates.add(builder.equal(root.get("type"), type));
        }
        return this;
    }

    public SaleSpecificationBuilder setSaleScope(SaleScope saleScope) {
        if (saleScope != null) {
            predicates.add(builder.equal(root.get("saleScope"), saleScope));
        }
        return this;
    }

    public SaleSpecificationBuilder setPromotionType(PromotionType promotionType) {
        if (promotionType != null) {
            predicates.add(builder.equal(root.get("promotionType"), promotionType));
        }
        return this;
    }

    public SaleSpecificationBuilder setIsIntroduceCustomer(Boolean isIntroduceCustomer) {
        if (isIntroduceCustomer != null) {
            predicates.add(builder.equal(root.get("isIntroduceCustomer"), isIntroduceCustomer));
        }
        return this;
    }

    public SaleSpecificationBuilder setIntroduceCustomerScope(IntroduceCustomerScope introduceCustomerScope) {
        if (introduceCustomerScope != null) {
            predicates.add(builder.equal(root.get("introduceCustomerScope"), introduceCustomerScope));
        }
        return this;
    }
}
