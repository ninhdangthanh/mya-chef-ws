package vn.com.ids.myachef.business.service;

import java.util.List;

import vn.com.ids.myachef.business.dto.CustomerOfSalePackageDTO;
import vn.com.ids.myachef.business.dto.SalePackageCustomerDTO;
import vn.com.ids.myachef.business.dto.SalePackageDetailDTO;
import vn.com.ids.myachef.dao.model.SalePackageDetailModel;

public interface SalePackageDetailService extends IGenericService<SalePackageDetailModel, Long> {

    public SalePackageDetailDTO create(SalePackageDetailDTO salePackageDetailDTO);

    public List<SalePackageCustomerDTO> showByCustomer(Long id);
    
    public CustomerOfSalePackageDTO showCustomerBoughtPackage(Long salePackageId);

}
