package vn.com.ids.myachef.business.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class DinnerTableDTO {
	
	private Long id;

	private String tableNumber;

	private Status status;

	private List<OrderDTO> orderDTOs = new ArrayList<>();
}
