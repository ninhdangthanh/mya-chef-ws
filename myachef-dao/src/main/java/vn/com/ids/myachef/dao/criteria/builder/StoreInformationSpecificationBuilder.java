package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import vn.com.ids.myachef.dao.model.StoreInformationModel;

public class StoreInformationSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<StoreInformationModel> root;
    private final CriteriaBuilder builder;

    public StoreInformationSpecificationBuilder(Root<StoreInformationModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public StoreInformationSpecificationBuilder setDefault(Boolean isDefault) {
        if (isDefault != null) {
            predicates.add(builder.equal(root.get("isDefault"), isDefault));
        }
        return this;
    }

}
