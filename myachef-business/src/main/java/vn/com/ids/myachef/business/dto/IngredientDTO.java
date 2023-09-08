package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class IngredientDTO {
	private Long id;

	private String name;

	private Integer price;

	private String image;

	private Double quantity;

	private String unit;

	private String description;

	private LocalDateTime createdDate;

	private LocalDateTime modifiedDate;

	private Status status; // active là còn, in_active là hết

	private IngredientCategoryDTO ingredientCategoryDTO;
	
	private Long ingredientCategoryDTOId;

	private List<DishDetailDTO> dishDetailDTOs = new ArrayList<>();
}
