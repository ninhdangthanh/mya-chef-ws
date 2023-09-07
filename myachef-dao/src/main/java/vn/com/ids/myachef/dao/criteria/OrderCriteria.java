package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCriteria extends AbstractCriteria {

	private static final long serialVersionUID = 1L;
	
	private Double totalPayment;
	
	private Long staffId;
	
	private Long tableId;

}
