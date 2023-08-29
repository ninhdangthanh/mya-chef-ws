package vn.com.ids.myachef.business.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.FileUploadDTO;
import vn.com.ids.myachef.business.dto.SalePackageDTO;
import vn.com.ids.myachef.dao.criteria.SalePackageCriteria;
import vn.com.ids.myachef.dao.model.SalePackageModel;

public interface SalePackageService extends IGenericService<SalePackageModel, Long> {

    Page<SalePackageModel> search(SalePackageCriteria salePackageCriteria);

    SalePackageDTO create(SalePackageDTO salePackageDTO, Long customerId);

    SalePackageDTO update(SalePackageModel salePackageModel, SalePackageDTO salePackageDTO, Long customerId);

    void updateStatusInActiveByIds(List<Long> ids);

    List<SalePackageDTO> getActiveSalePackage();

    FileUploadDTO getBanner(Long id, HttpServletRequest request);

}
