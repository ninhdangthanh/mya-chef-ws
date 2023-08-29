package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.SecurityContextService;
import vn.com.ids.myachef.business.dto.HomeBannerConfigDTO;
import vn.com.ids.myachef.business.dto.HomeConfigDTO;
import vn.com.ids.myachef.business.payload.request.HomeConfigBannerRequest;
import vn.com.ids.myachef.business.payload.request.HomeConfigRequest;
import vn.com.ids.myachef.business.service.HomeConfigService;

@RestController
@RequestMapping("/api/home-config")
@Slf4j
public class HomeConfigController {

    @Autowired
    private HomeConfigService homeConfigService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SecurityContextService securityContextService;

    @Operation(summary = "Get all")
    @GetMapping("/get-all")
    public HomeConfigDTO getAll() {
        log.info("------------------ Get all - START ----------------");
        return homeConfigService.getAll(request, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Create or update")
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HomeConfigDTO createOrUpdate(@RequestPart HomeConfigRequest homeConfigRequest, //
            @RequestParam(value = "banners", required = false) List<MultipartFile> banners) {
        log.info("------------------ Create or update - START ----------------");
        return homeConfigService.createOrUpdate(homeConfigRequest, banners, request, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Get interstitial ad banner")
    @GetMapping(value = "/interstitial-ad")
    public HomeBannerConfigDTO findInterstitialAdBanner() {
        log.info("------------------ Get interstitial ad banner - START ----------------");
        return homeConfigService.findInterstitialAdBanner(request, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Create or update interstitial ad banner")
    @PatchMapping(value = "/interstitial-ad", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HomeBannerConfigDTO createOrUpdateInterstitialAdBanner(@RequestPart(required = false) HomeConfigBannerRequest interstitialAdBannerInfo, //
            @RequestParam(value = "bannerFile", required = false) MultipartFile bannerFile) {
        log.info("------------------ Create or update interstitial ad banner - START ----------------");
        return homeConfigService.createOrUpdateInterstitialAdBanner(interstitialAdBannerInfo, bannerFile, request,
                securityContextService.getAuthenticatedUserId());
    }
}
