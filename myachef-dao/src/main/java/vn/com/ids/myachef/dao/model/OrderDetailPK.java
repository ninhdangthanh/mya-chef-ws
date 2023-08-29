package vn.com.ids.myachef.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OrderDetailPK)) {
            return false;
        }
        OrderDetailPK castOther = (OrderDetailPK) other;
        return (this.orderId == castOther.orderId) && (this.productId == castOther.productId);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int) (this.orderId ^ (this.orderId >>> 32)));
        hash = hash * prime + ((int) (this.productId ^ (this.productId >>> 32)));

        return hash;
    }
}