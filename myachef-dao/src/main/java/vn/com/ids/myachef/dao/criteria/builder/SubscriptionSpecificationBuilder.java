package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.model.SubscriptionModel;

public class SubscriptionSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<SubscriptionModel> root;
    private final CriteriaBuilder builder;

    public SubscriptionSpecificationBuilder(Root<SubscriptionModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public SubscriptionSpecificationBuilder setName(String name) {
        if (StringUtils.hasText(name)) {
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public SubscriptionSpecificationBuilder setTagLine(String tagLine) {
        if (StringUtils.hasText(tagLine)) {
            predicates.add(builder.like(root.get("tagLine"), "%" + tagLine + "%"));
        }
        return this;
    }

}
