package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartCriteria extends AbstractCriteria {

    private static final long serialVersionUID = 1L;

    private String productId;

    private Long userId;
}
