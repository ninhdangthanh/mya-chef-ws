package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.ProductConfigModel;

public class ProductConfigSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<ProductConfigModel> root;
    private final CriteriaBuilder builder;

    public ProductConfigSpecificationBuilder(Root<ProductConfigModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public ProductConfigSpecificationBuilder setName(String name) {
        if (StringUtils.hasText(name)) {
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public ProductConfigSpecificationBuilder setNhanhVnId(String nhanhVnId) {
        if (StringUtils.hasText(nhanhVnId)) {
            predicates.add(builder.equal(root.get("nhanhVnId"), nhanhVnId));
        }
        return this;
    }

    public ProductConfigSpecificationBuilder setNhanhVnCategoryId(String nhanhVnCategoryId) {
        if (StringUtils.hasText(nhanhVnCategoryId)) {
            predicates.add(builder.equal(root.get("nhanhVnCategoryId"), nhanhVnCategoryId));
        }
        return this;
    }

    public ProductConfigSpecificationBuilder setStatus(Status status) {
        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        }
        return this;
    }

    public ProductConfigSpecificationBuilder setNewProduct(Boolean isNew) {
        if (isNew != null) {
            predicates.add(builder.equal(root.get("isNewProduct"), isNew));
        }
        return this;
    }

    public ProductConfigSpecificationBuilder setIgnoreRelatedProduct(Long ignoreRelatedProductNhanhVnId) {
        if (ignoreRelatedProductNhanhVnId != null) {
            Expression<Boolean> jsonContainsPredicate = builder.function("JSON_CONTAINS", Boolean.class, root.get("relatedNhanhVnProductIds"),
                    builder.literal("\"" + ignoreRelatedProductNhanhVnId + "\""), builder.literal("$"));
            predicates.add(builder.isFalse(jsonContainsPredicate));
        }
        return this;
    }
}
