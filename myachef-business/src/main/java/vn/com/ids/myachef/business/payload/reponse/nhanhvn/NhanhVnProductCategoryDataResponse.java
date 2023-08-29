package vn.com.ids.myachef.business.payload.reponse.nhanhvn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NhanhVnProductCategoryDataResponse {

    private String id;

    private String parentId;

    private String name;

    private String code;

    private String order;

    private String showHome;

    private String showHomeOrder;

    private String privateId;

    private int status;

    private String image;

    private String content;
}
