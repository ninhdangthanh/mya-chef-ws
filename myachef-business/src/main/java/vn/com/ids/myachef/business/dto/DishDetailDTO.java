package vn.com.ids.myachef.business.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class DishDetailDTO {
	
	private Long id;

	private Double quantity;

	private Status status;

	private DishDTO dishDTO;

	private IngredientDTO ingredientDTO;
}
