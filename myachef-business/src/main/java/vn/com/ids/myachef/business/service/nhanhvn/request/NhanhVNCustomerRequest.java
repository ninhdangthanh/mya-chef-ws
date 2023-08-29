package vn.com.ids.myachef.business.service.nhanhvn.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NhanhVNCustomerRequest {

    // {\"name\":\"Dương Quá\",\"mobile\":\"0989899822\",\"cityName\":\"Hà Nội\",\"districtName\":\"Quận Đống
    // Đa\",\"wardName\":\"Phường Trung Liệt\"}

    private String name;
    private String mobile;
    
    private String cityName;
    private String districtName;
    private String wardName;
    private String address;
}
