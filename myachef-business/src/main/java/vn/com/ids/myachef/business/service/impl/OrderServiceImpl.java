package vn.com.ids.myachef.business.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.repository.OrderRepository;

@Service
@Transactional
public class OrderServiceImpl extends AbstractService<OrderModel, Long> implements OrderService {

	private OrderRepository orderRepository;

	protected OrderServiceImpl(OrderRepository orderRepository) {
		super(orderRepository);
		this.orderRepository = orderRepository;
	}

}
