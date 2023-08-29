package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.ProductCategoryConfigDTO;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductCategoryDataResponse;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.ProductCategoryConfigModel;

@Component
public class ProductCategoryConfigConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private FileUploadService fileUploadService;

    public List<ProductCategoryConfigDTO> toBasicDTOs(List<ProductCategoryConfigModel> models, HttpServletRequest request) {
        List<ProductCategoryConfigDTO> productCategoryConfigDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (ProductCategoryConfigModel productCategoryConfigModel : models) {
                productCategoryConfigDTOs.add(toBasicDTO(productCategoryConfigModel, request));
            }
        }
        return productCategoryConfigDTOs;
    }

    public ProductCategoryConfigDTO toBasicDTO(ProductCategoryConfigModel productCategoryConfigModel, HttpServletRequest request) {
        ProductCategoryConfigDTO productCategoryConfigDTO = null;
        if (productCategoryConfigModel != null) {
            productCategoryConfigDTO = mapper.map(productCategoryConfigModel, ProductCategoryConfigDTO.class);
            if (StringUtils.hasText(productCategoryConfigModel.getBanner())) {
                productCategoryConfigDTO.setBanner(
                        fileUploadService.getFilePath(applicationConfig.getBannerProductCategoryPath(), productCategoryConfigModel.getBanner(), request));
            }
        }
        return productCategoryConfigDTO;
    }

    public ProductCategoryConfigModel toModel(@Valid ProductCategoryConfigDTO productCategoryConfigDTO) {
        productCategoryConfigDTO.setStatus(Status.ACTIVE);
        return mapper.map(productCategoryConfigDTO, ProductCategoryConfigModel.class);
    }

    public List<ProductCategoryConfigModel> toModels(@Valid List<ProductCategoryConfigDTO> categoryDTOs) {
        List<ProductCategoryConfigModel> categoryModels = new ArrayList<>();
        if (!CollectionUtils.isEmpty(categoryDTOs)) {
            for (ProductCategoryConfigDTO categoryDTO : categoryDTOs) {
                categoryModels.add(toModel(categoryDTO));
            }
        }
        return categoryModels;
    }

    public List<ProductCategoryConfigDTO> toDTOs(List<ProductCategoryConfigModel> productCategoryConfigModels, HttpServletRequest request, Long customerId) {
        List<ProductCategoryConfigDTO> productCategoryConfigDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(productCategoryConfigModels)) {
            for (ProductCategoryConfigModel productCategoryConfigModel : productCategoryConfigModels) {
                productCategoryConfigDTOs.add(toDTO(productCategoryConfigModel, request, customerId));
            }
        }
        return productCategoryConfigDTOs;
    }

    public ProductCategoryConfigDTO toDTO(ProductCategoryConfigModel productCategoryConfigModel, HttpServletRequest request, Long customerId) {
        ProductCategoryConfigDTO productCategoryConfigDTO = toBasicDTO(productCategoryConfigModel, request);
        productCategoryConfigDTO.setProducts(productConfigService.findAllByNhanhVnCategoryId(productCategoryConfigModel.getNhanhVnId(), customerId));
        return productCategoryConfigDTO;
    }

    public ProductCategoryConfigDTO convertCategoryFromNhanhVnToDTO(NhanhVnProductCategoryDataResponse productCategoryDataResponse) {
        ProductCategoryConfigDTO categoryConfigDTO = new ProductCategoryConfigDTO();

        categoryConfigDTO.setNhanhVnId(productCategoryDataResponse.getId());
        categoryConfigDTO.setName(productCategoryDataResponse.getName());
        categoryConfigDTO.setImage(productCategoryDataResponse.getImage());
        categoryConfigDTO.setContent(productCategoryDataResponse.getContent());
        categoryConfigDTO.setStatus(productCategoryDataResponse.getStatus() == 1 ? Status.ACTIVE : Status.IN_ACTIVE);

        return categoryConfigDTO;
    }
}
