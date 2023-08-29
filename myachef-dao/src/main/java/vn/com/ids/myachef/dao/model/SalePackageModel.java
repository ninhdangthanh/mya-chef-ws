package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.SalePackageCondition;
import vn.com.ids.myachef.dao.enums.SalePackagePeriod;
import vn.com.ids.myachef.dao.enums.Status;

@Entity
@Table(name = "sale_package")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SalePackageModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer periodTime;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "period")
    @Enumerated(EnumType.STRING)
    private SalePackagePeriod period;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer totalProduct;

    @Type(type = "json")
    @Column(name = "product_ids", columnDefinition = "LONGTEXT")
    private Set<Long> productIds;

    @Column(name = "`condition`")
    @Enumerated(EnumType.STRING)
    private SalePackageCondition condition;

    private Double conditionMoney;

    @ToString.Exclude
    @OneToMany(mappedBy = "salePackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalePackageDetailModel> salePackages = new ArrayList<>();
}
