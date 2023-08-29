package vn.com.ids.myachef.business.service.nhanhvn.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NhanhVNOrderCreateResponse extends NhanhVNAbstractResponse{

    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    public class Data {
        private int orderId;
        private int shipFee;
        private int codFee;
    }

}
