package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.SecurityContextService;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.ProductCategoryConfigConverter;
import vn.com.ids.myachef.business.dto.ProductCategoryConfigDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.ProductCategoryConfigService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.ProductCategoryConfigCriteria;
import vn.com.ids.myachef.dao.model.ProductCategoryConfigModel;

@RestController
@RequestMapping("/api/product-category-config")
@Slf4j
@Validated
public class ProductCategoryConfigController {

    @Autowired
    private ProductCategoryConfigService productCategoryConfigService;

    @Autowired
    private ProductCategoryConfigConverter productCategoryConfigConverter;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SecurityContextService securityContextService;

    @Operation(summary = "search by criteria")
    @GetMapping("/search")
    public Page<ProductCategoryConfigDTO> getByCriteria(@ParameterObject ProductCategoryConfigCriteria productCategoryConfigCriteria) {
        log.info("------------------ Search, pagination - START ----------------");
        Page<ProductCategoryConfigModel> page = productCategoryConfigService.search(productCategoryConfigCriteria);
        List<ProductCategoryConfigDTO> productCategoryConfigDTOs = productCategoryConfigConverter.toBasicDTOs(page.getContent(), request);
        Pageable pageable = PageRequest.of(productCategoryConfigCriteria.getPageIndex(), productCategoryConfigCriteria.getPageSize());
        log.info("------------------ Search, pagination - END ----------------");
        return new PageImpl<>(productCategoryConfigDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by nhanhVnId")
    @GetMapping("/{nhanhVnId}")
    public ProductCategoryConfigDTO findByNhanhVnId(@PathVariable String nhanhVnId) {
        log.info("------------------ Find by nhanhVnId - START ----------------");
        ProductCategoryConfigModel productCategoryConfigModel = productCategoryConfigService.findByNhanhVnId(nhanhVnId);
        if (productCategoryConfigModel == null) {
            throw new ResourceNotFoundException("Not found product category with nhanhVnId: " + nhanhVnId);
        }
        log.info("------------------ Find by nhanhVnId - END ----------------");
        return productCategoryConfigConverter.toDTO(productCategoryConfigModel, request, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Find by combo")
    @GetMapping("/combo")
    public ProductCategoryConfigDTO findByCombo() {
        log.info("------------------ Find by combo - START ----------------");
        log.info("------------------ Find by combo - END ----------------");
        return productCategoryConfigService.findCombo(request, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Create")
    @PostMapping
    @Validated(OnCreate.class)
    public List<ProductCategoryConfigDTO> create(@RequestBody @Valid List<ProductCategoryConfigDTO> productCategoryConfigDTOs) {
        log.info("------------------ Create - START ----------------");
        List<ProductCategoryConfigModel> productCategoryConfigModels = productCategoryConfigService.create(productCategoryConfigDTOs);
        log.info("------------------ Create - END ----------------");
        return productCategoryConfigConverter.toBasicDTOs(productCategoryConfigModels, request);
    }

    @Operation(summary = "Update banner")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductCategoryConfigDTO updateBanner(@PathVariable Long id, @RequestParam(value = "banner", required = false) MultipartFile banner) {
        log.info("------------------ Update banner - START ----------------");
        ProductCategoryConfigModel productCategoryConfigModel = productCategoryConfigService.findOne(id);
        if (productCategoryConfigModel == null) {
            throw new ResourceNotFoundException("Not found product category with id: " + id);
        }
        return productCategoryConfigService.updateBanner(productCategoryConfigModel, banner, request);
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam List<Long> ids) {
        log.info("ids: {}", ids);
        if (!CollectionUtils.isEmpty(ids)) {
            List<ProductCategoryConfigModel> productCategoryConfigModels = productCategoryConfigService.findAllByIds(ids);
            if (!CollectionUtils.isEmpty(productCategoryConfigModels)) {
                for (ProductCategoryConfigModel productCategoryConfigModel : productCategoryConfigModels) {
                    fileStorageService.delete(applicationConfig.getFullBannerProductCategoryPath() + productCategoryConfigModel.getBanner());
                }

                productCategoryConfigService.deleteAll(productCategoryConfigModels);
            }
        }

        log.info("------------------ Delete - END ----------------");
    }

    @Operation(summary = "Get category from nhanhVN")
    @GetMapping(value = "/get-from-nhanhvn")
    public List<ProductCategoryConfigDTO> getCategoryFromNhanhVN() {
        log.info("------------------ Get categories from nhanhVN - START ----------------");
        return productCategoryConfigService.getCategoryFromNhanhVNs();
    }
}
