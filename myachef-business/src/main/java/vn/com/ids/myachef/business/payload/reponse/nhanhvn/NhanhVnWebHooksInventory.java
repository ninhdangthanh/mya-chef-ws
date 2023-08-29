package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnWebHooksInventory {

    private Long remain;

    private Long shipping;

    private Long damaged;

    private Long holding;
}
