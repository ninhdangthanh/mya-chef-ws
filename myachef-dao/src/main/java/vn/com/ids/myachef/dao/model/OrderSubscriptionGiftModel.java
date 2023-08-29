package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
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
import vn.com.ids.myachef.dao.enums.OrderSubscriptionGiftType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_subscription_gift")
@EntityListeners(AuditingEntityListener.class)
public class OrderSubscriptionGiftModel implements Serializable {

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

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OrderSubscriptionGiftType type;

    private Long subscriptionCustomerDetailId;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private OrderModel order;
}
