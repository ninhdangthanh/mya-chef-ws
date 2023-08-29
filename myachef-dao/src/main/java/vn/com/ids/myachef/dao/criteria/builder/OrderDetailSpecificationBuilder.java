package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import vn.com.ids.myachef.dao.model.OrderDetailModel;

public class OrderDetailSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<OrderDetailModel> root;
    private final CriteriaBuilder builder;

    public OrderDetailSpecificationBuilder(Root<OrderDetailModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
