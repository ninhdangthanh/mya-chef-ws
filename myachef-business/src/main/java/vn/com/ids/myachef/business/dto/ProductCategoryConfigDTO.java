package vn.com.ids.myachef.business.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.enums.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryConfigDTO {

    private Long id;

    @NotBlank(message = "Field 'name': {notblank}", groups = { OnCreate.class })
    private String name;

    private String image;

    @NotBlank(message = "Field 'nhanhVnId': {notblank}", groups = { OnCreate.class })
    private String nhanhVnId;

    private String content;

    private Status status;

    private String banner;

    private List<ProductConfigDTO> products;

    private boolean existInDatabase;
}
