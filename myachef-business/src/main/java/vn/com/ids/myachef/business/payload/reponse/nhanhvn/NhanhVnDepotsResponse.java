package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnDepotsResponse {

    private String remain;

    private String shipping;

    private String damaged;

    private String holding;

    private String warranty;

    private String warrantyHolding;

    private String available;
}
