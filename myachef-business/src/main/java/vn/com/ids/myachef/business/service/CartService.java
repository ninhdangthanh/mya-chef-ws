package vn.com.ids.myachef.business.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;

import vn.com.ids.myachef.business.dto.CartDTO;
import vn.com.ids.myachef.dao.criteria.CartCriteria;
import vn.com.ids.myachef.dao.model.CartModel;
import vn.com.ids.myachef.dao.repository.extended.model.CartProduct;

public interface CartService extends IGenericService<CartModel, Long> {

    Page<CartDTO> findByCriteria(CartCriteria cartCriteria, Long customerId);

    CartDTO create(@Valid CartDTO cartDTO, Long customerId);

    CartDTO update(CartModel cartModel, CartDTO cartDTO, Long customerId);

    List<Object[]> findQuantityAndProductByCartIds(List<Long> cartIds);

    List<CartModel> findByUserId(Long userId);

    List<CartProduct> findQuantityAndProductByCartIdIn(List<Long> cartIds);

    void updatePreSelectFalseByIdIn(List<Long> cartIds);
}
