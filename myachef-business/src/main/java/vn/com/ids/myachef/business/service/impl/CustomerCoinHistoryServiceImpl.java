package vn.com.ids.myachef.business.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.service.CustomerCoinHistoryService;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryScope;
import vn.com.ids.myachef.dao.enums.CustomerCoinHistoryType;
import vn.com.ids.myachef.dao.model.CustomerCoinHistoryModel;
import vn.com.ids.myachef.dao.model.CustomerModel;
import vn.com.ids.myachef.dao.repository.CustomerCoinHistoryRepository;

@Service
@Transactional
public class CustomerCoinHistoryServiceImpl extends AbstractService<CustomerCoinHistoryModel, Long> implements CustomerCoinHistoryService {

    private CustomerCoinHistoryRepository customerCoinHistoryRepository;

    protected CustomerCoinHistoryServiceImpl(CustomerCoinHistoryRepository customerCoinHistoryRepository) {
        super(customerCoinHistoryRepository);
        this.customerCoinHistoryRepository = customerCoinHistoryRepository;
    }

    @Autowired
    private CustomerService customerService;

    @Override
    public void updateCoinForCustomer(CustomerModel customerModel, long coin, CustomerCoinHistoryScope scope, Long scopeId, CustomerCoinHistoryType type) {
        customerModel.setCoin(customerModel.getCoin() + coin);

        CustomerCoinHistoryModel customerCoinHistoryModel = new CustomerCoinHistoryModel();
        customerCoinHistoryModel.setCustomerFullName(customerModel.getFullName());
        customerCoinHistoryModel.setCustomerId(customerModel.getId());
        customerCoinHistoryModel.setQuantity(coin);
        customerCoinHistoryModel.setScope(scope);
        customerCoinHistoryModel.setScopeId(scopeId);
        customerCoinHistoryModel.setType(type);

        customerService.save(customerModel);

        save(customerCoinHistoryModel);
    }
}
