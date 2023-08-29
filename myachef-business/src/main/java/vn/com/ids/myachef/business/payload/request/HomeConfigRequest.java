package vn.com.ids.myachef.business.payload.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeConfigRequest {

    private String dailyQuestion;

    private String notification;

    private List<Long> removeBannerIds = new ArrayList<>();

    private List<HomeConfigBannerRequest> slidersCreateInfo = new ArrayList<>();

    private List<HomeConfigBannerRequest> bodiesCreateInfo = new ArrayList<>();
}
