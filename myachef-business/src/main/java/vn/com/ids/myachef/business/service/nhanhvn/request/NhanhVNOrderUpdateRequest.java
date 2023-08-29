package vn.com.ids.myachef.business.service.nhanhvn.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NhanhVNOrderUpdateRequest {
    private String orderId;
    private String description;
    private String privateDescription;
    private String status;
}
