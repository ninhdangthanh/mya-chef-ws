package vn.com.ids.myachef.dao.criteria;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class IngredientCriteria extends AbstractCriteria {

	private static final long serialVersionUID = 1L;

	private String name;

	private Double price;

	private String image;

	private Double quantity;

	private String unit;

	private String description;

	private Status status;
	
	private Long ingredientCategoryId;

}
