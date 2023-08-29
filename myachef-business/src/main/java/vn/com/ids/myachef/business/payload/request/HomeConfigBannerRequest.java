package vn.com.ids.myachef.business.payload.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.dao.enums.HomeBannerConfigType;

@Getter
@Setter
public class HomeConfigBannerRequest {

    private Long homeConfigId;

    private Long typeId;

    private String fileName;

    private int order;

    private HomeBannerConfigType type;

    private List<HomeCategoryProductConfigRequest> productConfigs;
}
