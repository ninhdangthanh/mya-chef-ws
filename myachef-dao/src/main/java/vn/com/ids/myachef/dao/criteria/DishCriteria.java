package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.DishStatus;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class DishCriteria extends AbstractCriteria {

	private static final long serialVersionUID = 1L;

	private int price;

	private String priceLabel;

	private String image;

	private String description;

	private Status status;

	private DishStatus diskStatus;
	
	private Long dishCategoryId;
}
