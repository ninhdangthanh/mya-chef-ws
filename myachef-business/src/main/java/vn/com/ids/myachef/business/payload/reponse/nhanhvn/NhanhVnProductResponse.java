package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnProductResponse {
    private int code;

    private NhanhVnProductDataResponse data;

    private String message;
}
