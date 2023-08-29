package vn.com.ids.myachef.dao.criteria.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.enums.UserRole;
import vn.com.ids.myachef.dao.enums.UserStatus;
import vn.com.ids.myachef.dao.model.UserModel;

public class UserSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<UserModel> root;
    private final CriteriaBuilder builder;

    public UserSpecificationBuilder(Root<UserModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public UserSpecificationBuilder setSearchText(String searchText) {
        if (StringUtils.hasText(searchText)) {
            predicates.add(builder.like(root.get("searchText"), "%" + searchText + "%"));
        }
        return this;
    }

    public UserSpecificationBuilder setStatus(UserStatus status) {
        if (status != null) {
            predicates.add(builder.equal(root.get("status"), status));
        }
        return this;
    }

    public UserSpecificationBuilder setTimeFrame(LocalDateTime from, LocalDateTime to) {
        if (from != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), from));
        }
        if (to != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), to));
        }
        return this;
    }

    public UserSpecificationBuilder setRole(UserRole role) {
        if (role != null) {
            predicates.add(builder.equal(root.get("role"), role));
        }
        return this;
    }

}
