package vn.com.ids.myachef.business.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeCategoryProductConfigRequest {

    private Long productId;

    private int order;
}
