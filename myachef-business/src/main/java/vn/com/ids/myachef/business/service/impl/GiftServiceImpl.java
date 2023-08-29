package vn.com.ids.myachef.business.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.converter.SaleConverter;
import vn.com.ids.myachef.business.dto.SaleDTO;
import vn.com.ids.myachef.business.service.GiftService;
import vn.com.ids.myachef.business.service.SaleService;
import vn.com.ids.myachef.dao.model.GiftModel;
import vn.com.ids.myachef.dao.model.SaleModel;
import vn.com.ids.myachef.dao.repository.GiftRepository;

@Service
@Transactional
public class GiftServiceImpl extends AbstractService<GiftModel, Long> implements GiftService {

    private GiftRepository giftRepository;

    protected GiftServiceImpl(GiftRepository giftRepository) {
        super(giftRepository);
        this.giftRepository = giftRepository;
    }

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleConverter saleConverter;

    @Override
    public List<SaleDTO> findGiftByCustomerIdAndCondition(Long customerId, List<String> nhanhVnProductIds, double amount) {
        List<SaleModel> saleModels = saleService.findGiftByCustomerId(customerId);
        return saleConverter.toDTOsAndCheckCanUse(saleModels, customerId, nhanhVnProductIds, amount);
    }

}
