package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.model.CartModel;

public class CartSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<CartModel> root;
    private final CriteriaBuilder builder;

    public CartSpecificationBuilder(Root<CartModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public CartSpecificationBuilder setUserId(Long userId) {
        if (userId != null) {
            predicates.add(builder.equal(root.get("userId"), userId));
        }
        return this;
    }

    public CartSpecificationBuilder setProductId(String productId) {
        if (StringUtils.hasText(productId)) {
            predicates.add(builder.equal(root.get("nhanhVnProductId"), productId));
        }
        return this;
    }
}
