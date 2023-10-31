package vn.com.ids.myachef.business.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
public class DinnerTableDTO {
	
	private Long id;

	@NotBlank(message = "Field 'tableName': {notblank}", groups = { OnCreate.class })
	private String tableName;
	
	private Boolean enoughtFood;

	private Status status;

	private List<OrderDTO> orderDTOs = new ArrayList<>();
}
