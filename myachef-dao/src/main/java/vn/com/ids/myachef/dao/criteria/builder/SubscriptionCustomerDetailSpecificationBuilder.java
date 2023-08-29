package vn.com.ids.myachef.dao.criteria.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import vn.com.ids.myachef.dao.enums.OnlinePaymentType;
import vn.com.ids.myachef.dao.enums.PaymentStatus;
import vn.com.ids.myachef.dao.enums.SubscriptionCustomerType;
import vn.com.ids.myachef.dao.model.SubscriptionCustomerDetailModel;

public class SubscriptionCustomerDetailSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<SubscriptionCustomerDetailModel> root;
    private final CriteriaBuilder builder;

    public SubscriptionCustomerDetailSpecificationBuilder(Root<SubscriptionCustomerDetailModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public SubscriptionCustomerDetailSpecificationBuilder setTimeFrame(LocalDateTime from, LocalDateTime to) {

        if (from != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), from));
        }
        if (to != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), to));
        }

        return this;
    }

    public SubscriptionCustomerDetailSpecificationBuilder setPaymentStatus(PaymentStatus paymentStatus) {
        if (paymentStatus != null) {
            predicates.add(builder.equal(root.get("paymentStatus"), paymentStatus));
        }
        return this;
    }

    public SubscriptionCustomerDetailSpecificationBuilder setPaymentType(OnlinePaymentType paymentType) {
        if (paymentType != null) {
            predicates.add(builder.equal(root.get("paymentType"), paymentType));
        }
        return this;
    }

    public SubscriptionCustomerDetailSpecificationBuilder setSubscription(Long subscriptionId) {
        if (subscriptionId != null) {
            predicates.add(builder.equal(root.join("subscription").get("id"), subscriptionId));
        }
        return this;
    }

    public SubscriptionCustomerDetailSpecificationBuilder setCustomer(Long customerId) {
        if (customerId != null) {
            predicates.add(builder.equal(root.join("customer").get("id"), customerId));
        }
        return this;
    }

    public SubscriptionCustomerDetailSpecificationBuilder setType(SubscriptionCustomerType type) {
        if (type != null) {
            predicates.add(builder.equal(root.get("type"), type));
        }
        return this;
    }
}
