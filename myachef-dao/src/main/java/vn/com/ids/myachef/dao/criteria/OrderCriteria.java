package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.OrderStatus;

@Getter
@Setter
public class OrderCriteria extends AbstractCriteria {

	private static final long serialVersionUID = 1L;
	
	private Double totalPayment;

	private String imagePayment;
	
	private OrderStatus status;
	
	private Long staffId;
	
	private Long tableId;

}
