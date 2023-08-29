package vn.com.ids.myachef.business.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.service.SubscriptionDetailService;
import vn.com.ids.myachef.dao.model.SubscriptionDetailModel;
import vn.com.ids.myachef.dao.repository.SubscriptionDetailRepository;

@Transactional
@Service
public class SubscriptionDetailServiceImpl extends AbstractService<SubscriptionDetailModel, Long> implements SubscriptionDetailService {

    private SubscriptionDetailRepository subscriptionDetailRepository;

    protected SubscriptionDetailServiceImpl(SubscriptionDetailRepository subscriptionDetailRepository) {
        super(subscriptionDetailRepository);
        this.subscriptionDetailRepository = subscriptionDetailRepository;
    }

    @Override
    public List<SubscriptionDetailModel> findBySubscriptionCustomerDetailIds(List<Long> subscriptionCustomerDetailIds) {
        return subscriptionDetailRepository.findBySubscriptionCustomerDetailIds(subscriptionCustomerDetailIds);
    }
}
