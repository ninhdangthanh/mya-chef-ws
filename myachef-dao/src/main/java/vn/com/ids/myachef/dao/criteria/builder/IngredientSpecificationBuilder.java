package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.IngredientModel;

public class IngredientSpecificationBuilder {
    
    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<IngredientModel> root;
    private final CriteriaBuilder builder;
    
    public IngredientSpecificationBuilder(Root<IngredientModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }
    
    public IngredientSpecificationBuilder setName(String name) {
        if (StringUtils.hasText(name)) {
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }
    
    public IngredientSpecificationBuilder setPrice(Double price) {
        if (price != null && price > 0) {
            predicates.add(builder.equal(root.get("price"), price));
        }
        return this;
    }
    
    public IngredientSpecificationBuilder setImage(String image) {
        if (StringUtils.hasText(image)) {
            predicates.add(builder.like(root.get("image"), "%" + image + "%"));
        }
        return this;
    }
    
    public IngredientSpecificationBuilder setQuantity(Double quantity) {
        if (quantity != null) {
            predicates.add(builder.equal(root.get("quantity"), quantity));
        }
        return this;
    }
    
    public IngredientSpecificationBuilder setUnit(String unit) {
        if (StringUtils.hasText(unit)) {
            predicates.add(builder.like(root.get("unit"), "%" + unit + "%"));
        }
        return this;
    }
    
    public IngredientSpecificationBuilder setDescription(String description) {
        if (StringUtils.hasText(description)) {
            predicates.add(builder.like(root.get("description"), "%" + description + "%"));
        }
        return this;
    }
    
    public IngredientSpecificationBuilder setStatus(Status status) {
        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        }
        return this;
    }

}
