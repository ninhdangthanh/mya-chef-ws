package vn.com.ids.myachef.business.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import vn.com.ids.myachef.business.validation.group.OnCreate;

@Data
public class CartDTO {

    private Long id;

    private int quantity;

    private Long userId;

    @NotBlank(message = "Field 'nhanhVnProductId': {notblank}", groups = { OnCreate.class })
    private String nhanhVnProductId;

    private ProductConfigDTO product;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Boolean preSelect;
}
