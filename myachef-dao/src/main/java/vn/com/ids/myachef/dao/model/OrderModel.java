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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.OrderPaymentMethod;
import vn.com.ids.myachef.dao.enums.OrderStatus;
import vn.com.ids.myachef.dao.enums.OrderType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`order`")
@EntityListeners(AuditingEntityListener.class)
public class OrderModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nhanhVnId;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "note")
    private String note;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "recipient_child_name")
    private String recipientChildName;

    @Column(name = "recipient_child_gender")
    private Boolean recipientChildGender;

    @Column(name = "recipient_phone_number")
    private String recipientPhoneNumber;

    @Column(name = "recipient_city")
    private String recipientCity;

    @Column(name = "recipient_district")
    private String recipientDistrict;

    @Column(name = "recipient_ward")
    private String recipientWard;

    @Column(name = "recipient_city_id")
    private Integer recipientCityId;

    @Column(name = "recipient_district_id")
    private Integer recipientDistrictId;

    @Column(name = "recipient_ward_id")
    private Integer recipientWardId;

    @Column(name = "recipient_address")
    private String recipientAddress;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Column(name = "total_money")
    private double totalMoney = 0;

    private double totalAmount; // Tổng tiền hàng
    private double shipFee;

    private double totalMoneyDiscount; // Voucher giảm giá
    private double totalMammnyCoinReceive;
    private double shipDiscount;

    private double totalShipFee;

    private long coinDiscount;
    private double totalPayment;// Tổng số tiền

    private boolean isSyncToNhanhVn;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private OrderPaymentMethod paymentMethod;

    @Column(name = "payment_image")
    private String paymentImage;

    @Type(type = "json")
    @Column(name = "voucher_ids", columnDefinition = "LONGTEXT")
    private List<Long> voucherIds = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerModel customer;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailModel> orderDetails = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderSubscriptionGiftModel> subscriptionGifts = new ArrayList<>();

    public OrderDetailModel addOrderDetail(OrderDetailModel orderDetail) {
        getOrderDetails().add(orderDetail);
        orderDetail.setOrder(this);
        return orderDetail;
    }

    public OrderDetailModel removeOrderDetail(OrderDetailModel orderDetail) {
        getOrderDetails().remove(orderDetail);
        orderDetail.setOrder(null);
        return orderDetail;
    }

    public void addCustomer(CustomerModel customer) {
        this.setCustomer(customer);
        customer.getOrders().add(this);
    }

}
