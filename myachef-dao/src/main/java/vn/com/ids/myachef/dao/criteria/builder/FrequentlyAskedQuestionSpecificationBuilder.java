package vn.com.ids.myachef.dao.criteria.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.model.FrequentlyAskedQuestionModel;

public class FrequentlyAskedQuestionSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<FrequentlyAskedQuestionModel> root;
    private final CriteriaBuilder builder;

    public FrequentlyAskedQuestionSpecificationBuilder(Root<FrequentlyAskedQuestionModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public FrequentlyAskedQuestionSpecificationBuilder setTitle(String title) {
        if (StringUtils.hasText(title)) {
            predicates.add(builder.like(root.get("title"), "%" + title + "%"));
        }
        return this;
    }

    public FrequentlyAskedQuestionSpecificationBuilder setTimeFrame(LocalDateTime from, LocalDateTime to) {
        if (from != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), from));
        }
        if (to != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), to));
        }

        return this;
    }
}
