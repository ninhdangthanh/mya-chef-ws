package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import vn.com.ids.myachef.dao.enums.IntroduceCustomerScope;
import vn.com.ids.myachef.dao.enums.PromotionType;
import vn.com.ids.myachef.dao.enums.SaleScope;
import vn.com.ids.myachef.dao.enums.SaleType;
import vn.com.ids.myachef.dao.enums.Status;

@Entity
@Table(name = "sale")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SaleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    private String description;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SaleType type;

    @Column(name = "sale_scope")
    @Enumerated(EnumType.STRING)
    private SaleScope saleScope;

    @Column(name = "promotion_type")
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;

    private Double minimumOrderPrice;

    private Long totalQuantity;

    private Long maxQuantityUseInUser;

    private Long totalQuantityUsed;

    private Long maxPromotion;

    private Double discount;

    private Double discountPercent;

    private Double receiveMoneyPercent;

    private Double receiveMammyCoin;

    private boolean isIntroduceCustomer = false;

    private boolean isUpdatePrice = false;

    private boolean isRevertPrice = false;

    @Column(name = "introduce_customer_scope")
    @Enumerated(EnumType.STRING)
    private IntroduceCustomerScope introduceCustomerScope;

    @Type(type = "json")
    @Column(name = "used_user_id", columnDefinition = "LONGTEXT")
    private List<Long> usedUserId = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleDetailModel> saleDetails = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GiftModel> gifts = new ArrayList<>();
}
