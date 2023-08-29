package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnInventoryResponse {

    private Long remain;

    private Long shipping;

    private Long damaged;

    private Long holding;

    private Long available;

    private Long warranty;

    private Long warrantyHolding;

    private Map<String, NhanhVnDepotsResponse> depots;
}
