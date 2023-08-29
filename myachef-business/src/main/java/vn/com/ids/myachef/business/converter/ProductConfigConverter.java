package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.business.dto.ProductConfigDTO;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductDataDetailResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductDetailResponse;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.SystemConfigService;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.ProductConfigModel;

@Component
public class ProductConfigConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private CustomerService customerService;

    public List<ProductConfigDTO> toBasicDTOs(List<ProductConfigModel> models, Long customerId) {
        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        List<ProductConfigDTO> productConfigDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (ProductConfigModel productConfigModel : models) {
                productConfigDTOs.add(toBasicDTO(productConfigModel, isNewCustomer));
            }
        }
        return productConfigDTOs;
    }

    public ProductConfigDTO toBasicDTO(ProductConfigModel model, boolean isNewCustomer) {
        ProductConfigDTO productConfigDTO = mapper.map(model, ProductConfigDTO.class);
        if (isNewCustomer && model.getSaleNewCustomerPrice() != -1) {
            productConfigDTO.setPrice(model.getSaleNewCustomerPrice());
            productConfigDTO.setSaleQuantity(model.getSaleNewCustomerQuantity());
        }
        return productConfigDTO;
    }

    public ProductConfigModel toModel(@Valid ProductConfigDTO productConfigDTO) {
        return mapper.map(productConfigDTO, ProductConfigModel.class);
    }

    public ProductConfigDTO convertProductFromNhanhVnToDTO(NhanhVnProductDataDetailResponse dataDetailResponse) {
        ProductConfigDTO productConfigModel = new ProductConfigDTO();

        productConfigModel.setNhanhVnId(dataDetailResponse.getIdNhanh());
        productConfigModel.setImage(dataDetailResponse.getImage());
        productConfigModel.setName(dataDetailResponse.getName());
        productConfigModel.setNhanhVnCategoryId(dataDetailResponse.getCategoryId());
        productConfigModel.setPrice(Double.valueOf(dataDetailResponse.getPrice() == null ? "0" : dataDetailResponse.getPrice()));
        productConfigModel
                .setStatus(dataDetailResponse.getStatus().equals("New") || dataDetailResponse.getStatus().equals("Active") ? Status.ACTIVE : Status.IN_ACTIVE);
        productConfigModel.setImportPrice(Double.valueOf(dataDetailResponse.getImportPrice() == null ? "0" : dataDetailResponse.getImportPrice()));
        productConfigModel.setOldPrice(Double.valueOf(dataDetailResponse.getOldPrice() == null ? "0" : dataDetailResponse.getOldPrice()));

        return productConfigModel;
    }

    public ProductConfigDTO convertProductFromNhanhVnToDTO(NhanhVnProductDetailResponse nhanhVnProductDetail, ProductConfigModel productConfigModel,
            Long customerId) {
        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        ProductConfigDTO productConfigDTO = new ProductConfigDTO();

        productConfigDTO.setNhanhVnId(nhanhVnProductDetail.getIdNhanh());
        productConfigDTO.setImage(nhanhVnProductDetail.getImage());
        productConfigDTO.setName(nhanhVnProductDetail.getName());
        productConfigDTO.setNhanhVnCategoryId(nhanhVnProductDetail.getCategoryId());
        productConfigDTO.setPrice(productConfigModel.getPrice());
        productConfigDTO.setStatus(productConfigModel.getStatus());
        productConfigDTO.setImportPrice(nhanhVnProductDetail.getImportPrice() == null ? 0 : nhanhVnProductDetail.getImportPrice());
        productConfigDTO.setOldPrice(nhanhVnProductDetail.getOldPrice() == null ? 0 : nhanhVnProductDetail.getOldPrice());
        if ((productConfigDTO.getOldPrice() == null || productConfigDTO.getOldPrice() == 0) && productConfigModel.getSalePriceBackup() != -1) {
            productConfigDTO.setOldPrice(productConfigModel.getSalePriceBackup());
        }
        productConfigDTO.setDescription(nhanhVnProductDetail.getDescription());

        productConfigDTO.setFreeShipDescription(systemConfigService.getFreeShipDescription());
        productConfigDTO.setImages(nhanhVnProductDetail.getImages());

        productConfigDTO.setRelatedProducts(mapRelatedProductFormNhanhVnId(productConfigDTO.getNhanhVnId(), isNewCustomer));

        if (isNewCustomer && productConfigModel.getSaleNewCustomerPrice() != -1) {
            productConfigDTO.setPrice(productConfigModel.getSaleNewCustomerPrice());
            productConfigDTO.setSaleQuantity(productConfigModel.getSaleNewCustomerQuantity());
        }

        return productConfigDTO;
    }

    private List<ProductConfigDTO> mapRelatedProductFormNhanhVnId(String nhanhVnId, boolean isNewCustomer) {
        List<ProductConfigDTO> productConfigDTOs = new ArrayList<>();
        ProductConfigModel productConfigModel = productConfigService.findByNhanhVnId(nhanhVnId);
        if (productConfigModel != null && !CollectionUtils.isEmpty(productConfigModel.getRelatedNhanhVnProductIds())) {
            productConfigDTOs = mapRelatedProduct(productConfigModel.getRelatedNhanhVnProductIds(), isNewCustomer);
        }
        return productConfigDTOs;
    }

    public ProductConfigDTO toDTO(ProductConfigModel productConfigModel, Long customerId) {
        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        ProductConfigDTO productConfigDTO = toBasicDTO(productConfigModel, isNewCustomer);
        productConfigDTO.setRelatedProducts(mapRelatedProduct(productConfigModel.getRelatedNhanhVnProductIds(), isNewCustomer));
        return productConfigDTO;
    }

    private List<ProductConfigDTO> mapRelatedProduct(List<String> relatedNhanhVnProductIds, boolean isNewCustomer) {
        List<ProductConfigDTO> productConfigDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(relatedNhanhVnProductIds)) {
            List<String> limitRelatedProduct = relatedNhanhVnProductIds;
            if (relatedNhanhVnProductIds.size() > 4) {
                limitRelatedProduct = relatedNhanhVnProductIds.subList(relatedNhanhVnProductIds.size() - 4, relatedNhanhVnProductIds.size());
            }
            List<ProductConfigModel> productConfigModels = productConfigService.findByNhanhVnIdIn(limitRelatedProduct);
            if (!CollectionUtils.isEmpty(productConfigModels)) {
                productConfigDTOs = toBasicDTOAndIgnoreInActives(productConfigModels, isNewCustomer);
            }
        }
        return productConfigDTOs;
    }

    private List<ProductConfigDTO> toBasicDTOAndIgnoreInActives(List<ProductConfigModel> productConfigModels, boolean isNewCustomer) {
        List<ProductConfigDTO> productConfigDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(productConfigModels)) {
            for (ProductConfigModel productConfigModel : productConfigModels) {
                if (productConfigModel.getStatus() == Status.ACTIVE) {
                    productConfigDTOs.add(toBasicDTO(productConfigModel, isNewCustomer));
                }
            }
        }
        return productConfigDTOs;
    }

    public void toModel(ProductConfigModel productConfigModel, ProductConfigDTO productConfigDTO) {
        mapper.map(productConfigDTO, productConfigModel);
    }

    public List<ProductConfigDTO> toBasicDTOAndSetOrderForHomeConfigs(List<ProductConfigModel> models, Map<Long, Integer> mapOrderByProductId,
            boolean isNewCustomer) {
        List<ProductConfigDTO> productConfigDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(models)) {
            for (ProductConfigModel productConfigModel : models) {
                ProductConfigDTO productConfigDTO = toBasicDTO(productConfigModel, isNewCustomer);
                if (productConfigDTO != null) {
                    Integer order = mapOrderByProductId.get(productConfigModel.getId());
                    if (order != null) {
                        productConfigDTO.setHomeProductConfigOrder(order);
                    }
                    productConfigDTOs.add(productConfigDTO);
                }
            }
        }
        productConfigDTOs.sort(Comparator.comparing(ProductConfigDTO::getHomeProductConfigOrder));
        return productConfigDTOs;
    }
}
