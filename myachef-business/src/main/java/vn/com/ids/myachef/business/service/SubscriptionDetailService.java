package vn.com.ids.myachef.business.service;

import java.util.List;

import vn.com.ids.myachef.dao.model.SubscriptionDetailModel;

public interface SubscriptionDetailService extends IGenericService<SubscriptionDetailModel, Long> {

    List<SubscriptionDetailModel> findBySubscriptionCustomerDetailIds(List<Long> subscriptionCustomerDetailIds);

}
