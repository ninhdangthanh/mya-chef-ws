package vn.com.ids.myachef.dao.repository.extended.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.ids.myachef.dao.model.ProductConfigModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {
    private ProductConfigModel product;
    private int quantity;
    private boolean preSelect;
}
