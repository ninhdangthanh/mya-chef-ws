package vn.com.ids.myachef.business.service.nhanhvn.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NhanhVNOrderUpdateResponse extends NhanhVNAbstractResponse {

    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    public class Data {
        private String orderId;
        private String status;
        private Object shipFee;
        private Object codFee;
    }

}
