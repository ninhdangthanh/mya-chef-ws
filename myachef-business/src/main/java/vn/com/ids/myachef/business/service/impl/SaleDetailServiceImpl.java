package vn.com.ids.myachef.business.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.service.SaleDetailService;
import vn.com.ids.myachef.dao.model.SaleDetailModel;
import vn.com.ids.myachef.dao.repository.SaleDetailRepository;

@Service
@Transactional
public class SaleDetailServiceImpl extends AbstractService<SaleDetailModel, Long> implements SaleDetailService {

    private SaleDetailRepository saleDetailRepository;

    protected SaleDetailServiceImpl(SaleDetailRepository saleDetailRepository) {
        super(saleDetailRepository);
        this.saleDetailRepository = saleDetailRepository;
    }

    @Override
    public List<Long> findSaleIdByNhanhByNhanhVnProductIdIn(List<String> nhanhVnProductIds) {
        return saleDetailRepository.findSaleIdByNhanhByNhanhVnProductIdIn(nhanhVnProductIds);
    }

    @Override
    public List<Long> findProductIdBySaleId(Long saleId) {
        return saleDetailRepository.findProductIdBySaleId(saleId);
    }

    @Override
    public List<SaleDetailModel> findByProductId(Long id) {
        return saleDetailRepository.findByProductId(id);
    }
}
