package vn.com.ids.myachef.business.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.HomeBannerConfigDTO;
import vn.com.ids.myachef.business.dto.HomeConfigDTO;
import vn.com.ids.myachef.business.payload.request.HomeConfigBannerRequest;
import vn.com.ids.myachef.business.payload.request.HomeConfigRequest;
import vn.com.ids.myachef.dao.model.HomeConfigModel;

public interface HomeConfigService extends IGenericService<HomeConfigModel, Long> {

    HomeConfigDTO getAll(HttpServletRequest request, Long customerId);

    HomeConfigDTO createOrUpdate(HomeConfigRequest homeConfigRequest, List<MultipartFile> banners, HttpServletRequest request, Long customerId);

    HomeBannerConfigDTO createOrUpdateInterstitialAdBanner(HomeConfigBannerRequest interstitialAdBannerInfo, MultipartFile bannerFile,
            HttpServletRequest request, Long customerId);

    HomeBannerConfigDTO findInterstitialAdBanner(HttpServletRequest request, Long customerId);

}
