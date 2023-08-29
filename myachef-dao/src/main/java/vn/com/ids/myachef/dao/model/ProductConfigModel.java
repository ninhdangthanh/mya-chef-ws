package vn.com.ids.myachef.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.com.ids.myachef.dao.enums.Status;

@Entity
@Table(name = "product_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    @Column(columnDefinition = "double default -1", nullable = false)
    private double salePriceBackup = -1;

    @Column(columnDefinition = "double default -1", nullable = false)
    private double saleNewCustomerPrice = -1;

    private Double oldPrice;

    private Double importPrice;

    private String image;

    private String nhanhVnId;

    private String nhanhVnCategoryId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean isNewProduct = false;

    private long saleQuantity;

    private long saleNewCustomerQuantity;

    @Type(type = "json")
    @Column(name = "related_nhanh_vn_product_ids", columnDefinition = "LONGTEXT")
    private List<String> relatedNhanhVnProductIds = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleDetailModel> saleDetails = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalePackageDetailModel> salePackageDetails = new ArrayList<>();
}
