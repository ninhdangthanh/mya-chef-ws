package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.SubscriptionDetailType;

@Entity
@Table(name = "subscription_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SubscriptionDetailModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SubscriptionDetailType type;

    private String link;

    private String nhanhVnProductCategoryId;

    private Double giftMoney;

    private String nhanhVnProductId;

    private String description;

    private Integer quantity;

    private Long coin;

    @Column(name = "`limit`")
    private Integer limit;

    private Double minimumOrderPrice;

    @Type(type = "json")
    @Column(name = "condition_subscription_ids", columnDefinition = "LONGTEXT")
    private List<Long> conditionSubscriptionIds;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SubscriptionModel subscription;

    @ToString.Exclude
    @OneToMany(mappedBy = "subscriptionDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionCustomerDetailModel> subscriptionCustomerDetails = new ArrayList<>();
}
