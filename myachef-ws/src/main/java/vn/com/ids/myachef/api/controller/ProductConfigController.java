package vn.com.ids.myachef.api.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.json.JSONException;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.SecurityContextService;
import vn.com.ids.myachef.business.converter.ProductConfigConverter;
import vn.com.ids.myachef.business.dto.ProductConfigDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.ProductConfigCriteria;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.ProductConfigModel;

@RestController
@RequestMapping("/api/product-config")
@Slf4j
@Validated
public class ProductConfigController {

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private ProductConfigConverter productConfigConverter;

    @Autowired
    private SecurityContextService securityContextService;

    @Operation(summary = "search by criteria")
    @GetMapping("/search")
    public Page<ProductConfigDTO> getByCriteria(@ParameterObject ProductConfigCriteria productConfigCriteria) {
        log.info("------------------ Search, pagination - START ----------------");
        if (productConfigCriteria.getStatus() == null) {
            productConfigCriteria.setStatus(Status.ACTIVE);
        }
        Page<ProductConfigModel> page = productConfigService.search(productConfigCriteria);
        List<ProductConfigDTO> productConfigDTOs = productConfigConverter.toBasicDTOs(page.getContent(), securityContextService.getAuthenticatedUserId());
        Pageable pageable = PageRequest.of(productConfigCriteria.getPageIndex(), productConfigCriteria.getPageSize());
        log.info("------------------ Search, pagination - END ----------------");
        return new PageImpl<>(productConfigDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Get all relate product by id")
    @GetMapping("/all-related/{id}")
    public List<ProductConfigDTO> getAllRelateRroductById(@PathVariable Long id) {
        log.info("------------------ Get all relate product by id - START ----------------");
        return productConfigService.findAllRelateRroductById(id, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Get all paid product")
    @GetMapping("/all-product-paid")
    public List<ProductConfigDTO> findAllPaidProduct() {
        log.info("------------------ Get all paid product - START ----------------");
        return productConfigService.findAllPaidProduct(securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Check price changed")
    @PostMapping("/price-changed")
    public boolean checkPriceChanged(@RequestBody Map<String, Double> mapByNhanhVnIdAndDisplayPrice) {
        log.info("------------------ Check price changed - START ----------------");
        return productConfigService.checkPriceChanged(mapByNhanhVnIdAndDisplayPrice);
    }

    @Operation(summary = "Create")
    @PostMapping
    @Validated(OnCreate.class)
    public List<ProductConfigDTO> create(@RequestBody @Valid List<ProductConfigDTO> productConfigDTOs) {
        log.info("------------------ Create - START ----------------");
        List<ProductConfigModel> productConfigModels = productConfigService.create(productConfigDTOs);
        log.info("------------------ Create - END ----------------");
        return productConfigConverter.toBasicDTOs(productConfigModels, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Config new products")
    @PatchMapping("/new-products")
    public List<ProductConfigDTO> create(@RequestParam(required = false) List<Long> addIds, @RequestParam(required = false) List<Long> removeIds) {
        log.info("------------------ Config new products - START ----------------");
        if (!CollectionUtils.isEmpty(addIds)) {
            productConfigService.addNewProductByIds(addIds);
        }

        if (!CollectionUtils.isEmpty(removeIds)) {
            productConfigService.removeNewProductByIds(removeIds);
        }

        List<ProductConfigModel> productConfigModels = productConfigService.findByIsNewProductTrue();
        log.info("------------------ Config new products - END ----------------");
        return productConfigConverter.toBasicDTOs(productConfigModels, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Add related products")
    @PatchMapping("/related-products/{nhanhVnId}")
    public ProductConfigDTO relatedProducts(@PathVariable String nhanhVnId, @RequestBody List<String> relatedNhanhVnIds) {
        log.info("------------------ Add related products - START ----------------");
        ProductConfigModel productConfigModel = productConfigService.findByNhanhVnId(nhanhVnId);
        if (productConfigModel == null) {
            throw new ResourceNotFoundException("Not found product with nhanhVnId: " + nhanhVnId);
        }

        if (!CollectionUtils.isEmpty(relatedNhanhVnIds)) {
            productConfigModel.getRelatedNhanhVnProductIds().addAll(relatedNhanhVnIds);

            productConfigModel.setRelatedNhanhVnProductIds(new ArrayList<>(new LinkedHashSet<>(productConfigModel.getRelatedNhanhVnProductIds())));
            List<ProductConfigModel> productConfigModels = productConfigService.findByNhanhVnIdIn(relatedNhanhVnIds);
            if (!CollectionUtils.isEmpty(productConfigModels)) {
                for (ProductConfigModel productConfig : productConfigModels) {
                    if (!productConfig.getRelatedNhanhVnProductIds().contains(nhanhVnId)) {
                        productConfig.getRelatedNhanhVnProductIds().add(nhanhVnId);
                    }
                }

                productConfigService.saveAll(productConfigModels);
            }

            productConfigService.save(productConfigModel);
        }

        log.info("------------------ Add related products - END ----------------");
        return productConfigConverter.toDTO(productConfigModel, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Remove related products")
    @PatchMapping("/remove-related-products/{nhanhVnId}")
    public ProductConfigDTO RemoveRelatedProducts(@PathVariable String nhanhVnId, @RequestBody List<String> removeRelatedNhanhVnIds) {
        log.info("------------------ Add related products - START ----------------");
        ProductConfigModel productConfigModel = productConfigService.findByNhanhVnId(nhanhVnId);
        if (productConfigModel == null) {
            throw new ResourceNotFoundException("Not found product with nhanhVnId: " + nhanhVnId);
        }

        if (!CollectionUtils.isEmpty(removeRelatedNhanhVnIds)) {
            productConfigModel.getRelatedNhanhVnProductIds().removeAll(removeRelatedNhanhVnIds);

            List<ProductConfigModel> productConfigModels = productConfigService.findByNhanhVnIdIn(removeRelatedNhanhVnIds);
            if (!CollectionUtils.isEmpty(productConfigModels)) {
                for (ProductConfigModel productConfig : productConfigModels) {
                    if (productConfig.getRelatedNhanhVnProductIds().contains(nhanhVnId)) {
                        productConfig.getRelatedNhanhVnProductIds().remove(nhanhVnId);
                    }
                }

                productConfigService.saveAll(productConfigModels);
            }

            productConfigService.save(productConfigModel);
        }

        log.info("------------------ Add related products - END ----------------");
        return productConfigConverter.toDTO(productConfigModel, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam List<Long> ids) {
        log.info("ids: {}", ids);
        if (!CollectionUtils.isEmpty(ids)) {
            List<ProductConfigModel> productConfigModels = productConfigService.findAllById(ids);
            for (ProductConfigModel productConfigModel : productConfigModels) {
                productConfigModel.setStatus(Status.DELETED);

                List<ProductConfigModel> relatedProducts = productConfigService.findProductRelatedByNhanhVnId(productConfigModel.getNhanhVnId());
                if (!CollectionUtils.isEmpty(relatedProducts)) {
                    for (ProductConfigModel relatedProduct : relatedProducts) {
                        relatedProduct.getRelatedNhanhVnProductIds().remove(productConfigModel.getNhanhVnId());
                    }

                    productConfigService.saveAll(relatedProducts);
                }
            }
            productConfigService.saveAll(productConfigModels);
        }

        log.info("------------------ Delete - END ----------------");
    }

    @Operation(summary = "Get products from nhanhVN")
    @GetMapping(value = "/get-from-nhanhvn")
    public Page<ProductConfigDTO> getProductFromNhanhVNs(@RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize, //
            @RequestParam(required = false) String name, //
            @RequestParam(required = false) Integer categoryId) throws JSONException {
        log.info("------------------ Get Products From NhanhVN - START ----------------");
        return productConfigService.getProductFromNhanhVns(page, pageSize, name, categoryId);
    }

    @Operation(summary = "Get product detail from nhanhVN by nhanhVnId")
    @GetMapping(value = "/get-from-nhanhvn/{nhanhVnId}")
    public ProductConfigDTO getProductDetailFromNhanhVN(@PathVariable("nhanhVnId") String nhanhVnId) {
        log.info("------------------ Get Product Detail From NhanhVN by nhanhVnId - START ----------------");
        return productConfigService.getProductDetailFromNhanhVn(nhanhVnId, securityContextService.getAuthenticatedUserId());
    }

}
