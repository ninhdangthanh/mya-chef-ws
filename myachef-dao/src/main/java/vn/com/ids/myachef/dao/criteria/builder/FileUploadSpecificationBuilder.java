package vn.com.ids.myachef.dao.criteria.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import vn.com.ids.myachef.dao.model.FileUploadModel;

public class FileUploadSpecificationBuilder {

    private final List<Predicate> predicates = new ArrayList<>();
    private final Root<FileUploadModel> root;
    private final CriteriaBuilder builder;

    public FileUploadSpecificationBuilder(Root<FileUploadModel> root, CriteriaBuilder builder) {
        super();
        this.root = root;
        this.builder = builder;
    }

    public Predicate build() {
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    public FileUploadSpecificationBuilder setTimeFrame(LocalDateTime from, LocalDateTime to) {

        if (from != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), from));
        }
        if (to != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), to));
        }

        return this;
    }

    public FileUploadSpecificationBuilder setOriginalName(String originalName) {
        if (StringUtils.hasText(originalName)) {
            predicates.add(builder.like(root.get("originalName"), "%" + originalName + "%"));
        }
        return this;
    }

    public FileUploadSpecificationBuilder setGeneratedName(String generatedName) {
        if (StringUtils.hasText(generatedName)) {
            predicates.add(builder.like(root.get("generatedName"), "%" + generatedName + "%"));
        }
        return this;
    }

    public FileUploadSpecificationBuilder setBanner(Boolean isBanner) {
        if (isBanner != null) {
            predicates.add(builder.equal(root.get("isBanner"), isBanner));
        }
        return this;
    }

}
