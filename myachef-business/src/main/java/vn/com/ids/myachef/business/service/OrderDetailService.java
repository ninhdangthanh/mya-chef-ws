package vn.com.ids.myachef.business.service;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.dao.criteria.OrderDetailCriteria;
import vn.com.ids.myachef.dao.model.OrderDetailModel;

public interface OrderDetailService extends IGenericService<OrderDetailModel, Long> {
	
	public Page<OrderDetailModel> findAll(OrderDetailCriteria orderDetailCriteria);
}
