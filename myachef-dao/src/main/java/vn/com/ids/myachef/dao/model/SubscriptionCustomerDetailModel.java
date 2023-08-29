package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.OnlinePaymentType;
import vn.com.ids.myachef.dao.enums.PaymentStatus;
import vn.com.ids.myachef.dao.enums.SubscriptionCustomerType;

@Entity
@Table(name = "subscription_customer_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SubscriptionCustomerDetailModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private OnlinePaymentType paymentType;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SubscriptionCustomerType type;

    private LocalDate expiredDate;

    private String billImage;

    private String customerFullName;

    private String customerOrderPhoneNumber;

    private int giftQuantity;

    private int giftTotalUsed;

    private String giftDescription;

    @Column(name = "`limit`")
    private Integer limit;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SubscriptionModel subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private CustomerModel customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private SubscriptionDetailModel subscriptionDetail;
}