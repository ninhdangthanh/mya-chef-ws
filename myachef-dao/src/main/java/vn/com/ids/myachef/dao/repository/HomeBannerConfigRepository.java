package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.HomeBannerConfigModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface HomeBannerConfigRepository extends GenericRepository<HomeBannerConfigModel, Long>, JpaSpecificationExecutor<HomeBannerConfigModel> {

    HomeBannerConfigModel findByIsInterstitialAdTrue();

}
