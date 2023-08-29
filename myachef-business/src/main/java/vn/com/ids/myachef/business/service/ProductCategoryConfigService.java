package vn.com.ids.myachef.business.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.ProductCategoryConfigDTO;
import vn.com.ids.myachef.dao.criteria.ProductCategoryConfigCriteria;
import vn.com.ids.myachef.dao.model.ProductCategoryConfigModel;

public interface ProductCategoryConfigService extends IGenericService<ProductCategoryConfigModel, Long> {

    Page<ProductCategoryConfigModel> search(ProductCategoryConfigCriteria productCategoryConfigCriteria);

    List<ProductCategoryConfigModel> create(@Valid List<ProductCategoryConfigDTO> productCategoryConfigDTOs);

    void synchronyProductCategoryFromNhanhVN();

    List<ProductCategoryConfigDTO> getAllActiveIgnoreCombo(HttpServletRequest request, Long customerId);

    List<ProductCategoryConfigModel> findAllByIds(List<Long> ids);

    void deleteAll(List<ProductCategoryConfigModel> productCategoryConfigModels);

    ProductCategoryConfigDTO updateBanner(ProductCategoryConfigModel productCategoryConfigModel, MultipartFile banner, HttpServletRequest request);

    ProductCategoryConfigDTO findCombo(HttpServletRequest request, Long customerId);

    List<ProductCategoryConfigDTO> getCategoryFromNhanhVNs();

    ProductCategoryConfigModel findByNhanhVnId(String nhanhVnProductCategoryId);

}
