package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderEventData {
    private int businessId;
    private int orderId;
    private String shopOrderId;
    private String status;
    private String statusDescription;

}
