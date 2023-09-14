package vn.com.ids.myachef.business.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.OrderDetailStatus;

@Getter
@Setter
public class OrderDetailDTO {

    private Long id;

    private OrderDetailStatus status;
    
    private Integer quantity;

    private OrderDTO order;

    private DishDTO dish;
}
