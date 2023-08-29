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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetailModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderModel order;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductConfigModel product;

    private Double productCurrentPrice;

    private Double totalProductPrice;

    private String nhanhVnProductId;

    private int quantity = 0;

    private boolean isSubscriptionGift = false;

    public void addProductAndOrder(ProductConfigModel productModel, OrderModel orderModel, String nhanhVnProductId) {
        this.setProduct(productModel);
        this.setOrder(orderModel);
        this.nhanhVnProductId = nhanhVnProductId;
        this.productCurrentPrice = productModel.getPrice();
        this.totalProductPrice = productModel.getPrice() * this.quantity;
    }

}
