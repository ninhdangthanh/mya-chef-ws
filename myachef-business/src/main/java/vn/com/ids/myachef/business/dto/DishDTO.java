package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.DishStatus;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class DishDTO {
	private Long id;

	private String name;

	private int price;

	private String priceLabel;

	private String image;

	private String description;

	private LocalDateTime createdDate;

	private LocalDateTime modifiedDate;

	private Status status;

	private DishStatus diskStatus;

	private List<DishDetailDTO> dishDetailDTOs = new ArrayList<>();

	private DishCategoryDTO dishCategoryDTO;

	private OrderDTO orderDTO;
}
