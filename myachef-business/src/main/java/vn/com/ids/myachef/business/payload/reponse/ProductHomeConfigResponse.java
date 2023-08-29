package vn.com.ids.myachef.business.payload.reponse;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.dto.ProductConfigDTO;

@Getter
@Setter
public class ProductHomeConfigResponse {

    private List<ProductConfigDTO> products = new ArrayList<>();
}
