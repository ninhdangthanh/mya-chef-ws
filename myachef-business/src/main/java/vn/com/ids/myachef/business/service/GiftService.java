package vn.com.ids.myachef.business.service;

import java.util.List;

import vn.com.ids.myachef.business.dto.SaleDTO;
import vn.com.ids.myachef.dao.model.GiftModel;

public interface GiftService extends IGenericService<GiftModel, Long> {

    List<SaleDTO> findGiftByCustomerIdAndCondition(Long userId, List<String> nhanhVnProductIds, double amount);

}
