package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.time.LocalDate;
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
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.CustomerStatus;

@Entity
@Table(name = "`customers`")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CustomerModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nhanhVnId;

    @Column(name = "username")
    @Size(max = 255, message = "The max size of username is 255 characters.")
    private String username;

    @Column(name = "phone_number")
    @Size(max = 50, message = "The max size of Name is 50 characters.")
    private String phoneNumber;

    @Column(name = "full_name")
    @Size(max = 100, message = "The max size of fullName is 100 characters.")
    private String fullName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    @Column(name = "avatar")
    @Size(max = 255, message = "The max size of avatarFile is 255 characters.")
    private String avatar;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "search_text")
    @Size(max = 1000, message = "The max size of searchText is 1000 characters.")
    private String searchText;

    @Column(name = "zalo_access_token", columnDefinition = "TEXT")
    private String zaloAccessToken;

    @Column(name = "affiliate_code")
    private String affiliateCode;

    @Column(name = "referrer_code")
    private String referrerCode;

    @Column(name = "city")
    @Size(max = 255, message = "The max size of city is 255 characters.")
    private String city;

    private int cityId;

    @Column(name = "district")
    @Size(max = 255, message = "The max size of district is 255 characters.")
    private String district;

    private int districtId;

    @Column(name = "ward")
    @Size(max = 255, message = "The max size of ward is 255 characters.")
    private String ward;

    private int wardId;

    @Column(name = "address")
    @Size(max = 255, message = "The max size of address is 255 characters.")
    private String address;

    @Column(name = "child_name")
    private String childName;

    @Column(name = "child_gender")
    private Boolean childGender;

    @Column(name = "child_birthday")
    private LocalDate childBirthday;

    @Column(name = "is_follow_OA")
    private Boolean isFollowOA = false;

    private long coin;

    @ToString.Exclude
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderModel> orders = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionCustomerDetailModel> subscriptionCustomerDetails = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GiftModel> gifts = new ArrayList<>();
}
