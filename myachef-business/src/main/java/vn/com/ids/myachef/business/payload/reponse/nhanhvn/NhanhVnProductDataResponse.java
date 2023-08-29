package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnProductDataResponse {
    private int currentPage;
    private int totalPages;
    private Map<String, NhanhVnProductDataDetailResponse> products;
}
