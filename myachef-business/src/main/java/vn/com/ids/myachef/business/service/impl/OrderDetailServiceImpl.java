package vn.com.ids.myachef.business.service.impl;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.service.OrderDetailService;
import vn.com.ids.myachef.dao.criteria.OrderDetailCriteria;
import vn.com.ids.myachef.dao.criteria.builder.OrderDetailSpecificationBuilder;
import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.repository.OrderDetailRepository;

@Service
@Transactional
public class OrderDetailServiceImpl extends AbstractService<OrderDetailModel, Long> implements OrderDetailService {

	private OrderDetailRepository orderDetailRepository;

	protected OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
		super(orderDetailRepository);
		this.orderDetailRepository = orderDetailRepository;
	}
	
	public Specification<OrderDetailModel> buildSpecification(OrderDetailCriteria orderDetailCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new OrderDetailSpecificationBuilder(root, criteriaBuilder) //
                .build();
    }

    @Override
    public Page<OrderDetailModel> findAll(OrderDetailCriteria orderDetailCriteria) {
        Specification<OrderDetailModel> specification = buildSpecification(orderDetailCriteria);
        Pageable pageable = buildPageable(orderDetailCriteria);
        return orderDetailRepository.findAll(specification, pageable);
    }
	
}
