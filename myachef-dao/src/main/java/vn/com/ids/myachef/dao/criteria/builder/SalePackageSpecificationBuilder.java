package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.SalePackageModel;

public class SalePackageSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<SalePackageModel> root;
    private final CriteriaBuilder builder;

    public SalePackageSpecificationBuilder(Root<SalePackageModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public SalePackageSpecificationBuilder setName(String name) {
        if (StringUtils.hasText(name)) {
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public SalePackageSpecificationBuilder setStatus(Status status) {
        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        }
        return this;
    }
}
