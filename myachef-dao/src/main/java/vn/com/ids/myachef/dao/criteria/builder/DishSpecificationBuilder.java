package vn.com.ids.myachef.dao.criteria.builder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.enums.DishStatus;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.model.IngredientModel;

public class DishSpecificationBuilder {
    
    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<DishModel> root;
    private final CriteriaBuilder builder;
    
    public DishSpecificationBuilder(Root<DishModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }
    
    public DishSpecificationBuilder setName(String name) {
        if (StringUtils.hasText(name)) {
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }
    
    public DishSpecificationBuilder setPrice(Double price) {
        if (price != null && price > 0) {
            predicates.add(builder.equal(root.get("price"), price));
        }
        return this;
    }
    
    public DishSpecificationBuilder setPriceLabel(String priceLabel) {
        if (StringUtils.hasText(priceLabel)) {
            predicates.add(builder.like(root.get("priceLabel"), "%" + priceLabel + "%"));
        }
        return this;
    }
    
    public DishSpecificationBuilder setImage(String image) {
        if (StringUtils.hasText(image)) {
            predicates.add(builder.like(root.get("image"), "%" + image + "%"));
        }
        return this;
    }
    
    public DishSpecificationBuilder setDescription(String description) {
        if (StringUtils.hasText(description)) {
            predicates.add(builder.like(root.get("description"), "%" + description + "%"));
        }
        return this;
    }
    
    public DishSpecificationBuilder setStatus(Status status) {
        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        }
        return this;
    }
    
    public DishSpecificationBuilder setDishStatus(DishStatus dishStatus) {
        if (dishStatus != null) {
            predicates.add(builder.equal(root.get("dishStatus"), dishStatus));
        }
        return this;
    }
    
}
