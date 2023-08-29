package vn.com.ids.myachef.business.service;

import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryScope;
import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryType;
import vn.com.ids.myachef.dao.model.CustomerCoinHistoryModel;
import vn.com.ids.myachef.dao.model.CustomerModel;

public interface CustomerCoinHistoryService extends IGenericService<CustomerCoinHistoryModel, Long> {

    void updateCoinForCustomer(CustomerModel customerModel, long coin, CustomerCoinHistoryScope scope, Long scopeId, CustomerCoinHistoryType type);

}
