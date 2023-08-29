package vn.com.ids.myachef.dao.criteria.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.dao.enums.OrderPaymentMethod;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.enums.OrderType;
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

    public OrderSpecificationBuilder setTimeFrame(LocalDateTime from, LocalDateTime to) {
        if (from != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), from));
        }
        if (to != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), to));
        }

        return this;
    }

    public OrderSpecificationBuilder setStatuses(List<OrderStatus> statuses) {
        if (!CollectionUtils.isEmpty(statuses)) {
            predicates.add(root.get("status").in(statuses));
        }
        return this;
    }

    public OrderSpecificationBuilder setType(OrderType type) {
        if (type != null) {
            predicates.add(builder.equal(root.get("type"), type));
        }
        return this;
    }

    public OrderSpecificationBuilder setPaymentMethod(OrderPaymentMethod paymentMethod) {
        if (paymentMethod != null) {
            predicates.add(builder.equal(root.get("paymentMethod"), paymentMethod));
        }
        return this;
    }

    public OrderSpecificationBuilder setCustomerIds(List<Long> customerIds) {
        if (!CollectionUtils.isEmpty(customerIds)) {
            predicates.add(root.get("customer").get("id").in(customerIds));
        }
        return this;
    }

    public OrderSpecificationBuilder setMoneyFrame(Double from, Double to) {
        if (from != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("totalMoney"), from));
        }
        if (to != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("totalMoney"), to));
        }

        return this;
    }

}
