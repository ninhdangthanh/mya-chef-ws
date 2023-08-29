package vn.com.ids.myachef.business.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.com.ids.myachef.business.payload.request.HomeCategoryProductConfigRequest;
import vn.com.ids.myachef.dao.enums.HomeBannerConfigType;
import vn.com.ids.myachef.dao.enums.HomeConfigBannerPosition;

@Getter
@Setter
public class HomeBannerConfigDTO {

    private Long id;

    private Long typeId;

    private String bannerFileName;

    private String name;

    private String bannerUrl;

    private int order;

    private HomeConfigBannerPosition position;

    private HomeBannerConfigType type;

    private Object data;

    private String fileNameForUpdate;

    private List<HomeCategoryProductConfigRequest> productConfigsForUpdate;
}
