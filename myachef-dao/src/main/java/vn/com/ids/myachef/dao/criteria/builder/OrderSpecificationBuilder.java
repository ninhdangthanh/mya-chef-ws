package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.IngredientModel;
import vn.com.ids.myachef.dao.model.OrderModel;

public class OrderSpecificationBuilder {
    
    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<OrderModel> root;
    private final CriteriaBuilder builder;
    
    public OrderSpecificationBuilder(Root<OrderModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }
    
    public OrderSpecificationBuilder setImagePayment(String imagePayment) {
        if (StringUtils.hasText(imagePayment)) {
            predicates.add(builder.like(root.get("imagePayment"), "%" + imagePayment + "%"));
        }
        return this;
    }
    
    public OrderSpecificationBuilder setTotalPayment(Double totalPayment) {
        if (totalPayment != null && totalPayment > 0) {
            predicates.add(builder.equal(root.get("totalPayment"), totalPayment));
        }
        return this;
    }
    
    public OrderSpecificationBuilder setStatus(OrderStatus status) {
        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        }
        return this;
    }

}
