package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnProductCategoryResponse {

    private int code;

    List<NhanhVnProductCategoryDataResponse> data;

    private List<String> messages;
}
