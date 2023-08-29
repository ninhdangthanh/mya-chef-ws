package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.ProductCategoryConfigModel;

public class ProductCategoryConfigSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<ProductCategoryConfigModel> root;
    private final CriteriaBuilder builder;

    public ProductCategoryConfigSpecificationBuilder(Root<ProductCategoryConfigModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public ProductCategoryConfigSpecificationBuilder setName(String name) {
        if (StringUtils.hasText(name)) {
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public ProductCategoryConfigSpecificationBuilder setNhanhVnId(String nhanhVnId) {
        if (StringUtils.hasText(nhanhVnId)) {
            predicates.add(builder.equal(root.get("nhanhVnId"), nhanhVnId));
        }
        return this;
    }

    public ProductCategoryConfigSpecificationBuilder setStatus(Status status) {
        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        }
        return this;
    }
}
