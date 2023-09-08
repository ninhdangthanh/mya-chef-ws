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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.DishStatus;
import vn.com.ids.myachef.dao.enums.Status;

@Entity
@Table(name = "dish")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DishModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private String priceLabel;

    private String image;

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

    @Column(name = "dish_status")
    @Enumerated(EnumType.STRING)
    private DishStatus diskStatus;

    @ToString.Exclude
    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishDetailModel> dishDetails = new ArrayList<>();
    
    @ToString.Exclude
    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailModel> orderDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private DishCategoryModel dishCategory;
    
}
