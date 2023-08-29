package vn.com.ids.myachef.dao.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "sale_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double discount;

    private Double discountPercent;

    private Double maxPromotion;

    private Long totalQuantity;

    private Long totalQuantityUsed;

    private Long maxQuantityUseInUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SaleModel sale;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private ProductConfigModel product;
}
