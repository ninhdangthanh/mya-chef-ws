package vn.com.ids.myachef.business.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.HomeConfigConverter;
import vn.com.ids.myachef.business.dto.HomeBannerConfigDTO;
import vn.com.ids.myachef.business.dto.HomeConfigDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.payload.request.HomeCategoryProductConfigRequest;
import vn.com.ids.myachef.business.payload.request.HomeConfigBannerRequest;
import vn.com.ids.myachef.business.payload.request.HomeConfigRequest;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.HomeBannerConfigService;
import vn.com.ids.myachef.business.service.HomeConfigService;
import vn.com.ids.myachef.business.service.ProductCategoryConfigService;
import vn.com.ids.myachef.business.service.SaleService;
import vn.com.ids.myachef.business.service.SubscriptionService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.enums.HomeBannerConfigType;
import vn.com.ids.myachef.dao.enums.HomeConfigBannerPosition;
import vn.com.ids.myachef.dao.model.HomeBannerConfigModel;
import vn.com.ids.myachef.dao.model.HomeConfigModel;
import vn.com.ids.myachef.dao.model.ProductCategoryConfigModel;
import vn.com.ids.myachef.dao.model.SaleModel;
import vn.com.ids.myachef.dao.model.SubscriptionModel;
import vn.com.ids.myachef.dao.repository.HomeConfigRepository;

@Transactional
@Service
@Slf4j
public class HomeConfigServiceImpl extends AbstractService<HomeConfigModel, Long> implements HomeConfigService {

    private HomeConfigRepository homeConfigRepository;

    protected HomeConfigServiceImpl(HomeConfigRepository homeConfigRepository) {
        super(homeConfigRepository);
        this.homeConfigRepository = homeConfigRepository;
    }

    @Autowired
    private HomeConfigConverter homeConfigConverter;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private SaleService flashSaleService;

    @Autowired
    private HomeBannerConfigService homeBannerConfigService;

    @Autowired
    private ProductCategoryConfigService productCategoryConfigService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CustomerService customerService;

    @Override
    public HomeConfigDTO getAll(HttpServletRequest request, Long customerId) {
        HomeConfigDTO homeConfigDTO = null;
        List<HomeConfigModel> homeConfigModels = homeConfigRepository.findAll();
        if (!CollectionUtils.isEmpty(homeConfigModels)) {
            HomeConfigModel homeConfigModel = homeConfigModels.get(0);
            homeConfigDTO = homeConfigConverter.toDTO(homeConfigModel, request, customerId);
        }
        log.info("------------------ Get all - END ----------------");
        return homeConfigDTO;
    }

    @Override
    public HomeConfigDTO createOrUpdate(HomeConfigRequest homeConfigRequest, List<MultipartFile> banners, HttpServletRequest request, Long customerId) {
        List<HomeConfigModel> homeConfigModels = homeConfigRepository.findAll();
        HomeConfigModel homeConfigModel = null;

        Map<String, MultipartFile> mapBannerByFileName = null;
        if (!CollectionUtils.isEmpty(banners)) {
            mapBannerByFileName = banners.stream()
                    .collect(Collectors.toMap(MultipartFile::getOriginalFilename, file -> file, (existingFile, newFile) -> existingFile));
        }

        if (!CollectionUtils.isEmpty(homeConfigModels)) {
            // Update
            homeConfigModel = homeConfigModels.get(0);
            if (StringUtils.hasText(homeConfigRequest.getDailyQuestion())) {
                homeConfigModel.setDailyQuestion(homeConfigRequest.getDailyQuestion());
            }

            if (StringUtils.hasText(homeConfigRequest.getNotification())) {
                homeConfigModel.setNotification(homeConfigRequest.getNotification());
            }

            if (!CollectionUtils.isEmpty(homeConfigRequest.getRemoveBannerIds())) {
                for (int i = homeConfigModel.getHomeBannerConfigs().size() - 1; i >= 0; i--) {
                    HomeBannerConfigModel banner = homeConfigModel.getHomeBannerConfigs().get(i);
                    if (homeConfigRequest.getRemoveBannerIds().contains(banner.getId())) {
                        fileStorageService.delete(applicationConfig.getFullBannerPath() + banner.getBannerFileName());
                        homeConfigModel.getHomeBannerConfigs().remove(i);
                    }
                }
            }
        } else {
            // Create
            homeConfigModel = homeConfigConverter.create(homeConfigRequest);
        }

        Map<Long, HomeBannerConfigModel> mapHomeConfigById = homeConfigModel.getHomeBannerConfigs().stream()
                .collect(Collectors.toMap(HomeBannerConfigModel::getId, homeBannerConfig -> homeBannerConfig));

        if (!CollectionUtils.isEmpty(homeConfigRequest.getSlidersCreateInfo())) {
            for (HomeConfigBannerRequest homeConfigBannerRequest : homeConfigRequest.getSlidersCreateInfo()) {
                createHomeBannerConfigs(homeConfigBannerRequest, mapBannerByFileName, homeConfigModel, HomeConfigBannerPosition.SLIDER, mapHomeConfigById);
            }
        }

        if (!CollectionUtils.isEmpty(homeConfigRequest.getBodiesCreateInfo())) {
            for (HomeConfigBannerRequest homeConfigBannerRequest : homeConfigRequest.getBodiesCreateInfo()) {
                createHomeBannerConfigs(homeConfigBannerRequest, mapBannerByFileName, homeConfigModel, HomeConfigBannerPosition.BODY, mapHomeConfigById);
            }
        }

        homeConfigRepository.saveAndFlush(homeConfigModel);
        log.info("------------------ Create or update - END ----------------");
        return homeConfigConverter.toDTO(homeConfigModel, request, customerId);
    }

    private void createHomeBannerConfigs(HomeConfigBannerRequest homeConfigBannerRequest, Map<String, MultipartFile> mapBannerByFileName,
            HomeConfigModel homeConfigModel, HomeConfigBannerPosition position, Map<Long, HomeBannerConfigModel> mapHomeConfigById) {

        HomeBannerConfigModel homeBannerConfigModel = mapHomeConfigById.get(homeConfigBannerRequest.getHomeConfigId());
        if (homeBannerConfigModel == null) {
            homeBannerConfigModel = new HomeBannerConfigModel();
        }

        updateDataByType(homeConfigBannerRequest.getTypeId(), homeConfigBannerRequest.getProductConfigs(), homeConfigBannerRequest.getType(),
                homeBannerConfigModel);

        if (mapBannerByFileName != null) {
            MultipartFile bannerFile = mapBannerByFileName.get(homeConfigBannerRequest.getFileName());
            if (bannerFile != null) {

                if (StringUtils.hasText(homeBannerConfigModel.getBannerFileName())) {
                    fileStorageService.delete(applicationConfig.getFullBannerPath() + homeBannerConfigModel.getBannerFileName());
                }

                String prefixName = UUID.randomUUID().toString();
                String generatedName = String.format("%s%s%s", prefixName, "-", bannerFile.getOriginalFilename());
                fileStorageService.upload(applicationConfig.getFullBannerPath(), generatedName, bannerFile);

                homeBannerConfigModel.setBannerFileName(generatedName);

                homeBannerConfigService.save(homeBannerConfigModel);
            } else {
                log.error("Banner file null with name {}", homeConfigBannerRequest.getFileName());
            }
        }

        if (homeConfigBannerRequest.getOrder() > 0) {
            homeBannerConfigModel.setOrder(homeConfigBannerRequest.getOrder());
        }
        if (homeConfigBannerRequest.getType() != null) {
            homeBannerConfigModel.setType(homeConfigBannerRequest.getType());
        }
        homeBannerConfigModel.setPosition(position);
        homeBannerConfigModel.setHomeConfig(homeConfigModel);
        homeConfigModel.getHomeBannerConfigs().add(homeBannerConfigModel);
    }

    private void updateDataByType(Long typeId, List<HomeCategoryProductConfigRequest> productConfigs, HomeBannerConfigType type,
            HomeBannerConfigModel homeBannerConfigModel) {
        if (type != null) {
            switch (type) {
            case CATEGORY:
                if (typeId != null) {
                    ProductCategoryConfigModel productCategoryConfigModel = productCategoryConfigService.findOne(typeId);
                    if (productCategoryConfigModel != null) {
                        homeBannerConfigModel.setTypeId(productCategoryConfigModel.getId());
                        homeBannerConfigModel.setName(productCategoryConfigModel.getName());
                    } else {
                        log.error("Not found category with id {}", typeId);
                    }
                }
                if (productConfigs != null) {
                    homeBannerConfigModel.setBannerConfigProducts(homeConfigConverter.mapBannerConfigProducts(productConfigs));
                }
                break;
            case SALE:
                if (typeId != null) {
                    SaleModel saleModel = flashSaleService.findOne(typeId);
                    if (saleModel != null) {
                        homeBannerConfigModel.setTypeId(saleModel.getId());
                        homeBannerConfigModel.setName(saleModel.getName());
                    } else {
                        log.error("Not found sale with id {}", typeId);
                    }
                }
                break;
            case SUBSCRIPTION:
                if (typeId != null) {
                    SubscriptionModel subscriptionModel = subscriptionService.findOne(typeId);
                    if (subscriptionModel != null) {
                        homeBannerConfigModel.setTypeId(subscriptionModel.getId());
                        homeBannerConfigModel.setName(subscriptionModel.getName());
                    } else {
                        log.error("Not found subscription with id {}", typeId);
                        homeBannerConfigModel.setTypeId(null);
                        homeBannerConfigModel.setName(null);
                    }
                } else {
                    homeBannerConfigModel.setTypeId(null);
                    homeBannerConfigModel.setName(null);
                }
                break;
            case PRODUCT:
                if (productConfigs != null) {
                    homeBannerConfigModel.setBannerConfigProducts(homeConfigConverter.mapBannerConfigProducts(productConfigs));
                }
                break;

            default:
                break;
            }
        }
    }

    @Override
    public HomeBannerConfigDTO createOrUpdateInterstitialAdBanner(HomeConfigBannerRequest interstitialAdBannerInfo, MultipartFile bannerFile,
            HttpServletRequest request, Long customerId) {
        HomeBannerConfigModel homeBannerConfigModel = homeBannerConfigService.findByIsInterstitialAdTrue();
        if (homeBannerConfigModel == null) {
            homeBannerConfigModel = new HomeBannerConfigModel();
        }

        if (interstitialAdBannerInfo != null) {
            if (interstitialAdBannerInfo.getTypeId() != null) {
                updateDataByType(interstitialAdBannerInfo.getTypeId(), interstitialAdBannerInfo.getProductConfigs(), interstitialAdBannerInfo.getType(),
                        homeBannerConfigModel);
            }

            if (interstitialAdBannerInfo.getType() != null) {
                homeBannerConfigModel.setType(interstitialAdBannerInfo.getType());
            }
        }

        if (bannerFile != null) {

            if (StringUtils.hasText(homeBannerConfigModel.getBannerFileName())) {
                fileStorageService.delete(applicationConfig.getFullBannerPath() + homeBannerConfigModel.getBannerFileName());
            }

            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", bannerFile.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullBannerPath(), generatedName, bannerFile);

            homeBannerConfigModel.setBannerFileName(generatedName);
        }

        homeBannerConfigModel.setInterstitialAd(true);

        homeBannerConfigService.save(homeBannerConfigModel);

        log.info("------------------ Create or update interstitial ad banner - END ----------------");

        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        return homeConfigConverter.toDTO(homeBannerConfigModel, request, isNewCustomer, customerId);
    }

    @Override
    public HomeBannerConfigDTO findInterstitialAdBanner(HttpServletRequest request, Long customerId) {
        HomeBannerConfigModel homeBannerConfigModel = homeBannerConfigService.findByIsInterstitialAdTrue();
        if (homeBannerConfigModel == null) {
            throw new BadRequestException("Not found interstitial ad banner");
        }

        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        return homeConfigConverter.toDTO(homeBannerConfigModel, request, isNewCustomer, customerId);
    }
}
