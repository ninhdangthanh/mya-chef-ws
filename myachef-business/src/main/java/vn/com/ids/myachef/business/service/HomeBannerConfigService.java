package vn.com.ids.myachef.business.service;

import vn.com.ids.myachef.dao.model.HomeBannerConfigModel;

public interface HomeBannerConfigService extends IGenericService<HomeBannerConfigModel, Long> {

    HomeBannerConfigModel findByIsInterstitialAdTrue();

}
