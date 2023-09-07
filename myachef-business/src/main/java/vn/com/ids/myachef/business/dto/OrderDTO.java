package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
	private Long id;

	private Double totalPayment;

	private LocalDateTime createdDate;

	private LocalDateTime modifiedDate;

	private DinnerTableDTO dinnerTableDTO;

	private UserDTO userDTO;

	private List<DishDTO> dishDTOs = new ArrayList<>();
}
