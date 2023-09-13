package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.DishStatus;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class DishDTO {
	private Long id;

	private String name;

	private Double price;

	private String priceLabel;

	private String image;

	private String description;

	private LocalDateTime createdDate;

	private LocalDateTime modifiedDate;

	private Status status;

	private DishStatus dichStatus;

	private List<DishDetailDTO> dishDetailDTOs = new ArrayList<>();

	private DishCategoryDTO dishCategoryDTO;
	
	private Long dishCategoryId;

	private OrderDTO orderDTO;
	
	private Map<Long, Double> dishDetailHashMap = new HashMap<>(); // id : quantity
}
