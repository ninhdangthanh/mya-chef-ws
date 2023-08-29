package vn.com.ids.myachef.business.service;

import java.util.List;

import vn.com.ids.myachef.dao.model.SaleDetailModel;

public interface SaleDetailService extends IGenericService<SaleDetailModel, Long> {

    List<Long> findSaleIdByNhanhByNhanhVnProductIdIn(List<String> nhanhVnProductIds);

    List<Long> findProductIdBySaleId(Long saleId);

    List<SaleDetailModel> findByProductId(Long id);

}
