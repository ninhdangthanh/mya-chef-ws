package vn.com.ids.myachef.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.ProductCategoryConfigConverter;
import vn.com.ids.myachef.business.dto.ProductCategoryConfigDTO;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductCategoryDataResponse;
import vn.com.ids.myachef.business.payload.reponse.nhanhvn.NhanhVnProductCategoryResponse;
import vn.com.ids.myachef.business.service.NhanhVnAPIService;
import vn.com.ids.myachef.business.service.ProductCategoryConfigService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.criteria.ProductCategoryConfigCriteria;
import vn.com.ids.myachef.dao.criteria.builder.ProductCategoryConfigSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.ProductCategoryConfigModel;
import vn.com.ids.myachef.dao.repository.ProductCategoryConfigRepository;

@Service
@Transactional
@Slf4j
public class ProductCategoryConfigServiceImpl extends AbstractService<ProductCategoryConfigModel, Long> implements ProductCategoryConfigService {

    private ProductCategoryConfigRepository productCategoryConfigRepository;

    protected ProductCategoryConfigServiceImpl(ProductCategoryConfigRepository productCategoryConfigRepository) {
        super(productCategoryConfigRepository);
        this.productCategoryConfigRepository = productCategoryConfigRepository;
    }

    @Autowired
    private NhanhVnAPIService nhanhVnAPIService;

    @Autowired
    private ProductCategoryConfigConverter productCategoryConfigConverter;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApplicationConfig applicationConfig;

    private Specification<ProductCategoryConfigModel> buildSpecification(ProductCategoryConfigCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new ProductCategoryConfigSpecificationBuilder(root, criteriaBuilder) //
                .setName(criteria.getName()) //
                .setNhanhVnId(criteria.getNhanhVnId()) //
                .setStatus(criteria.getStatus()) //
                .build(); //
    }

    @Override
    public Page<ProductCategoryConfigModel> search(ProductCategoryConfigCriteria productCategoryConfigCriteria) {
        Pageable pageable = buildPageable(productCategoryConfigCriteria);
        Specification<ProductCategoryConfigModel> specification = buildSpecification(productCategoryConfigCriteria);
        return productCategoryConfigRepository.findAll(specification, pageable);
    }

    @Override
    public List<ProductCategoryConfigModel> create(@Valid List<ProductCategoryConfigDTO> productCategoryConfigDTOs) {
        List<ProductCategoryConfigModel> productCategoryConfigModels = productCategoryConfigConverter.toModels(productCategoryConfigDTOs);
        productCategoryConfigModels = saveAll(productCategoryConfigModels);
        return productCategoryConfigModels;
    }

    @Override
    public void synchronyProductCategoryFromNhanhVN() {
        NhanhVnProductCategoryResponse response = nhanhVnAPIService.getListCategory();
        if (response != null) {
            if (response.getCode() == 1 && !CollectionUtils.isEmpty(response.getData())) {
                Map<String, NhanhVnProductCategoryDataResponse> mapCategoryDataById = response.getData().stream()
                        .collect(Collectors.toMap(NhanhVnProductCategoryDataResponse::getId, data -> data));

                List<ProductCategoryConfigModel> productCategoryConfigModels = productCategoryConfigRepository.findAll();
                if (!CollectionUtils.isEmpty(productCategoryConfigModels)) {
                    updateData(productCategoryConfigModels, mapCategoryDataById);
                }
            } else {
                log.error("Nhanh vn error: {}", response.getMessages().toString());
            }
        }
    }

    private void updateData(List<ProductCategoryConfigModel> productCategoryConfigModels, Map<String, NhanhVnProductCategoryDataResponse> mapCategoryDataById) {
        List<ProductCategoryConfigModel> deleteProductCategoryConfigModels = new ArrayList<>();
        for (ProductCategoryConfigModel productCategoryConfigModel : productCategoryConfigModels) {
            NhanhVnProductCategoryDataResponse data = mapCategoryDataById.get(productCategoryConfigModel.getNhanhVnId());
            if (data != null) {
                // Update data
                productCategoryConfigModel.setContent(data.getContent());
                productCategoryConfigModel.setImage(data.getImage());
                productCategoryConfigModel.setName(data.getName());
                productCategoryConfigModel.setStatus(data.getStatus() == 1 ? Status.ACTIVE : Status.IN_ACTIVE);
            } else {
                // Category deleted remove category config
                deleteProductCategoryConfigModels.add(productCategoryConfigModel);
            }
        }

        productCategoryConfigRepository.saveAll(productCategoryConfigModels);

        if (!CollectionUtils.isEmpty(deleteProductCategoryConfigModels)) {
            deleteProductCategoryConfig(deleteProductCategoryConfigModels);
        }
    }

    private void deleteProductCategoryConfig(List<ProductCategoryConfigModel> deleteProductCategoryConfigModels) {
        List<String> nhanhVnIds = deleteProductCategoryConfigModels.stream().map(ProductCategoryConfigModel::getNhanhVnId).collect(Collectors.toList());
        productConfigService.removeNhanhVnCategoryIdByNhanhCategoryIds(nhanhVnIds);
        productCategoryConfigRepository.deleteAll(deleteProductCategoryConfigModels);
    }

    @Override
    public List<ProductCategoryConfigDTO> getAllActiveIgnoreCombo(HttpServletRequest request, Long customerId) {
        List<ProductCategoryConfigModel> productCategoryConfigModels = productCategoryConfigRepository.findAllIgnoreCombo();
        return productCategoryConfigConverter.toDTOs(productCategoryConfigModels, request, customerId);
    }

    @Override
    public List<ProductCategoryConfigModel> findAllByIds(List<Long> ids) {
        return productCategoryConfigRepository.findAllById(ids);
    }

    @Override
    public void deleteAll(List<ProductCategoryConfigModel> productCategoryConfigModels) {
        productCategoryConfigRepository.deleteAll(productCategoryConfigModels);
    }

    @Override
    public ProductCategoryConfigDTO updateBanner(ProductCategoryConfigModel productCategoryConfigModel, MultipartFile banner, HttpServletRequest request) {
        if (banner != null) {
            if (StringUtils.hasText(productCategoryConfigModel.getBanner())) {
                fileStorageService.delete(applicationConfig.getFullBannerProductCategoryPath() + productCategoryConfigModel.getBanner());
            }

            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", banner.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullBannerProductCategoryPath(), generatedName, banner);
            productCategoryConfigModel.setBanner(generatedName);
        }
        return productCategoryConfigConverter.toBasicDTO(productCategoryConfigModel, request);
    }

    @Override
    public ProductCategoryConfigDTO findCombo(HttpServletRequest request, Long customerId) {
        ProductCategoryConfigModel categoryConfigModel = productCategoryConfigRepository.findCombo();
        if (categoryConfigModel != null) {
            return productCategoryConfigConverter.toDTO(categoryConfigModel, request, customerId);
        }
        return null;
    }

    @Override
    public List<ProductCategoryConfigDTO> getCategoryFromNhanhVNs() {
        NhanhVnProductCategoryResponse response = nhanhVnAPIService.getListCategory();

        List<ProductCategoryConfigDTO> nhanhVnProductCategoryDTOs = new ArrayList<>();

        if (response != null && response.getCode() == 1 && !CollectionUtils.isEmpty(response.getData())) {
            List<String> nhanhVnCategoryIdsFromDb = productCategoryConfigRepository.getAllNhanhIds();

            for (NhanhVnProductCategoryDataResponse item : response.getData()) {
                ProductCategoryConfigDTO itemDTO = productCategoryConfigConverter.convertCategoryFromNhanhVnToDTO(item);
                itemDTO.setExistInDatabase(nhanhVnCategoryIdsFromDb.contains(item.getId()));
                nhanhVnProductCategoryDTOs.add(itemDTO);
            }
        }

        log.info("------------------ Get categories from nhanhVN - END ----------------");
        return nhanhVnProductCategoryDTOs;
    }

    @Override
    public ProductCategoryConfigModel findByNhanhVnId(String nhanhVnProductCategoryId) {
        return productCategoryConfigRepository.findByNhanhVnId(nhanhVnProductCategoryId);
    }
}
