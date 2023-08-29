package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.FileUploadDTO;
import vn.com.ids.myachef.business.dto.HomeBannerConfigDTO;
import vn.com.ids.myachef.business.dto.HomeConfigDTO;
import vn.com.ids.myachef.business.dto.ProductCategoryConfigDTO;
import vn.com.ids.myachef.business.dto.SaleDTO;
import vn.com.ids.myachef.business.dto.SubscriptionDTO;
import vn.com.ids.myachef.business.payload.reponse.HomeConfigSubscriptionResponse;
import vn.com.ids.myachef.business.payload.reponse.ProductHomeConfigResponse;
import vn.com.ids.myachef.business.payload.request.HomeCategoryProductConfigRequest;
import vn.com.ids.myachef.business.payload.request.HomeConfigRequest;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.business.service.ProductCategoryConfigService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.SaleService;
import vn.com.ids.myachef.business.service.SubscriptionService;
import vn.com.ids.myachef.dao.enums.HomeBannerConfigType;
import vn.com.ids.myachef.dao.enums.HomeConfigBannerPosition;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.FileUploadModel;
import vn.com.ids.myachef.dao.model.HomeBannerConfigModel;
import vn.com.ids.myachef.dao.model.HomeCategoryProductConfig;
import vn.com.ids.myachef.dao.model.HomeConfigModel;
import vn.com.ids.myachef.dao.model.ProductCategoryConfigModel;
import vn.com.ids.myachef.dao.model.SaleModel;
import vn.com.ids.myachef.dao.model.SubscriptionModel;

@Component
@Slf4j
public class HomeConfigConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private ProductConfigConverter productConfigConverter;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private ProductCategoryConfigService productCategoryConfigService;

    @Autowired
    private FileUploadConverter fileUploadConverter;

    @Autowired
    private SaleService saleService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SaleConverter saleConverter;

    @Autowired
    private SubscriptionConverter subscriptionConverter;

    @Autowired
    private CustomerService customerService;

    public HomeConfigDTO toDTO(HomeConfigModel homeConfigModel, HttpServletRequest request, Long customerId) {
        HomeConfigDTO homeConfigDTO = mapper.map(homeConfigModel, HomeConfigDTO.class);
        mapHomeBannerConfigs(homeConfigModel, homeConfigDTO, request, customerId);
        return homeConfigDTO;
    }

    public FileUploadDTO mapBanner(Long id, HttpServletRequest request) {
        FileUploadDTO fileUploadDTO = null;
        FileUploadModel fileUploadModel = fileUploadService.findOne(id);
        if (fileUploadModel != null) {
            fileUploadDTO = fileUploadConverter.toDTO(fileUploadModel, applicationConfig.getBannerPath(), request);
        }
        return fileUploadDTO;
    }

    private void mapHomeBannerConfigs(HomeConfigModel homeConfigModel, HomeConfigDTO homeConfigDTO, HttpServletRequest request, Long customerId) {
        if (!CollectionUtils.isEmpty(homeConfigModel.getHomeBannerConfigs())) {
            boolean isNewCustomer = false;
            if (customerId != null) {
                isNewCustomer = customerService.isNewCustomer(customerId);
            }

            for (HomeBannerConfigModel homeBannerConfigModel : homeConfigModel.getHomeBannerConfigs()) {
                HomeBannerConfigDTO homeBannerConfigDTO = mapper.map(homeBannerConfigModel, HomeBannerConfigDTO.class);
                if (StringUtils.hasText(homeBannerConfigModel.getBannerFileName())) {
                    homeBannerConfigDTO
                            .setBannerUrl(fileUploadService.getFilePath(applicationConfig.getBannerPath(), homeBannerConfigModel.getBannerFileName(), request));
                }
                if (homeBannerConfigModel.getPosition() == HomeConfigBannerPosition.BODY
                        || (homeBannerConfigModel.getPosition() == HomeConfigBannerPosition.SLIDER
                                && homeBannerConfigModel.getType() == HomeBannerConfigType.SALE)) {
                    mapDtoByType(homeBannerConfigModel, homeBannerConfigDTO, request, isNewCustomer, customerId);
                }
                if (homeBannerConfigModel.getPosition() == HomeConfigBannerPosition.BODY) {
                    homeConfigDTO.getBodies().add(homeBannerConfigDTO);
                } else if (homeBannerConfigModel.getPosition() == HomeConfigBannerPosition.SLIDER) {
                    homeConfigDTO.getBannerSliders().add(homeBannerConfigDTO);
                }
            }

            homeConfigDTO.getBodies().sort(Comparator.comparing(HomeBannerConfigDTO::getOrder));
            homeConfigDTO.getBannerSliders().sort(Comparator.comparing(HomeBannerConfigDTO::getOrder));
        }
    }

    private void mapDtoByType(HomeBannerConfigModel homeBannerConfigModel, HomeBannerConfigDTO homeBannerConfigDTO, HttpServletRequest request,
            boolean isNewCustomer, Long customerId) {
        switch (homeBannerConfigModel.getType()) {
        case CATEGORY:
            if (homeBannerConfigModel.getTypeId() != null) {
                ProductCategoryConfigModel productCategoryConfigModel = productCategoryConfigService.findOne(homeBannerConfigModel.getTypeId());
                if (productCategoryConfigModel != null) {
                    homeBannerConfigDTO.setData(mapCategoryData(productCategoryConfigModel, homeBannerConfigModel, isNewCustomer, customerId));
                } else {
                    log.error("Not found category with id {}", homeBannerConfigModel.getTypeId());
                }
            }
            break;
        case SALE:
            if (homeBannerConfigModel.getTypeId() != null) {
                SaleModel saleModel = saleService.findOne(homeBannerConfigModel.getTypeId());
                if (saleModel != null) {
                    homeBannerConfigDTO.setData(saleConverter.toDTO(saleModel, isNewCustomer));
                } else {
                    log.error("Not found sale with id {}", homeBannerConfigModel.getTypeId());
                }
            }
            break;
        case SUBSCRIPTION:
            if (homeBannerConfigModel.getTypeId() != null) {
                SubscriptionModel subscriptionModel = subscriptionService.findOne(homeBannerConfigModel.getTypeId());
                if (subscriptionModel != null) {
                    homeBannerConfigDTO.setData(mapper.map(subscriptionModel, SubscriptionDTO.class));
                } else {
                    log.error("Not found subscription with id {}", homeBannerConfigModel.getTypeId());
                }
            } else {
                List<SubscriptionModel> subscriptionModels = subscriptionService.findByStatus(Status.ACTIVE);
                homeBannerConfigDTO.setData(new HomeConfigSubscriptionResponse(null, subscriptionConverter.toBasicDTOs(subscriptionModels, request)));
            }
            break;
        case PRODUCT:
            homeBannerConfigDTO.setData(mapProductData(homeBannerConfigModel, isNewCustomer));
            break;
        case FLASH_SALE:
            List<SaleDTO> flashSales = saleService.getAllFlashSaleActive(customerId);
            if (!CollectionUtils.isEmpty(flashSales)) {
                homeBannerConfigDTO.setData(flashSales.get(0));
            } else {
                log.info("Not found flash sale is processing");
            }
            break;

        default:
            break;
        }
    }

    private ProductHomeConfigResponse mapProductData(HomeBannerConfigModel homeBannerConfigModel, boolean isNewCustomer) {
        ProductHomeConfigResponse response = new ProductHomeConfigResponse();
        if (!CollectionUtils.isEmpty(homeBannerConfigModel.getBannerConfigProducts())) {
            Map<Long, Integer> mapOrderByProductId = homeBannerConfigModel.getBannerConfigProducts().stream()
                    .collect(Collectors.toMap(HomeCategoryProductConfig::getProductId, HomeCategoryProductConfig::getOrder));
            response.setProducts(productConfigConverter.toBasicDTOAndSetOrderForHomeConfigs(
                    productConfigService.findAllById(new ArrayList<Long>(mapOrderByProductId.keySet())), mapOrderByProductId, isNewCustomer));
        }
        return response;
    }

    private ProductCategoryConfigDTO mapCategoryData(ProductCategoryConfigModel productCategoryConfigModel, HomeBannerConfigModel homeBannerConfigModel,
            boolean isNewCustomer, Long customerId) {
        ProductCategoryConfigDTO productCategoryConfigDTO = mapper.map(productCategoryConfigModel, ProductCategoryConfigDTO.class);
        if (homeBannerConfigModel.getBannerConfigProducts() != null) {
            if (homeBannerConfigModel.getBannerConfigProducts().isEmpty()) {
                productCategoryConfigDTO.setProducts(productConfigService.findAllByNhanhVnCategoryId(productCategoryConfigModel.getNhanhVnId(), customerId));
            } else {
                Map<Long, Integer> mapOrderByProductId = homeBannerConfigModel.getBannerConfigProducts().stream()
                        .collect(Collectors.toMap(HomeCategoryProductConfig::getProductId, HomeCategoryProductConfig::getOrder));
                productCategoryConfigDTO.setProducts(productConfigConverter.toBasicDTOAndSetOrderForHomeConfigs(
                        productConfigService.findAllById(new ArrayList<Long>(mapOrderByProductId.keySet())), mapOrderByProductId, isNewCustomer));
            }
        }
        return productCategoryConfigDTO;
    }

    public HomeConfigModel toModel(HomeConfigDTO homeConfigDTO) {
        return mapper.map(homeConfigDTO, HomeConfigModel.class);
    }

    public HomeConfigModel create(HomeConfigRequest homeConfigRequest) {
        return mapper.map(homeConfigRequest, HomeConfigModel.class);
    }

    public List<HomeCategoryProductConfig> mapBannerConfigProducts(List<HomeCategoryProductConfigRequest> homeCategoryProductConfigRequests) {
        List<HomeCategoryProductConfig> homeCategoryProductConfigs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(homeCategoryProductConfigRequests)) {
            for (HomeCategoryProductConfigRequest homeCategoryProductConfigRequest : homeCategoryProductConfigRequests) {
                homeCategoryProductConfigs.add(mapper.map(homeCategoryProductConfigRequest, HomeCategoryProductConfig.class));
            }
        }
        return homeCategoryProductConfigs;
    }

    public HomeBannerConfigDTO toDTO(HomeBannerConfigModel homeBannerConfigModel, HttpServletRequest request, boolean isNewCustomer, Long customerId) {
        HomeBannerConfigDTO homeBannerConfigDTO = mapper.map(homeBannerConfigModel, HomeBannerConfigDTO.class);
        if (StringUtils.hasText(homeBannerConfigModel.getBannerFileName())) {
            homeBannerConfigDTO
                    .setBannerUrl(fileUploadService.getFilePath(applicationConfig.getBannerPath(), homeBannerConfigModel.getBannerFileName(), request));
        }

        mapDtoByType(homeBannerConfigModel, homeBannerConfigDTO, request, isNewCustomer, customerId);

        return homeBannerConfigDTO;
    }
}
