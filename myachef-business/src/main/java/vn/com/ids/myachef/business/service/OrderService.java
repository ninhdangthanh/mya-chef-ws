package vn.com.ids.myachef.business.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.OrderDTO;
import vn.com.ids.myachef.dao.criteria.OrderCriteria;
import vn.com.ids.myachef.dao.model.OrderModel;

public interface OrderService extends IGenericService<OrderModel, Long> {

    Page<OrderModel> findAll(OrderCriteria orderCriteria);

    OrderDTO create(@Valid OrderDTO orderDTO);

    OrderDTO update(@Valid OrderDTO orderDTO, OrderModel orderModel, MultipartFile image);

    String addDish(OrderModel orderModel, Long dishId);

    String removeDish(OrderModel orderModel, Long dishId);

    OrderDTO completeOrder(OrderModel orderModel);

    OrderDTO uploadImagePayment(OrderModel orderModel, MultipartFile image);

    OrderDTO confirmBankPayment(OrderModel orderModel);

    OrderModel findOrderExistingByDinnerTableId(Long dinnerTableId);
}
