package vn.com.ids.myachef.business.service.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.CartConverter;
import vn.com.ids.myachef.business.dto.CartDTO;
import vn.com.ids.myachef.business.service.CartService;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.dao.criteria.CartCriteria;
import vn.com.ids.myachef.dao.criteria.builder.CartSpecificationBuilder;
import vn.com.ids.myachef.dao.model.CartModel;
import vn.com.ids.myachef.dao.repository.CartRepository;
import vn.com.ids.myachef.dao.repository.extended.model.CartProduct;

@Service
@Transactional
@Slf4j
public class CartServiceImpl extends AbstractService<CartModel, Long> implements CartService {

    private CartRepository cartRepository;

    protected CartServiceImpl(CartRepository cartRepository) {
        super(cartRepository);
        this.cartRepository = cartRepository;
    }

    @Autowired
    private CartConverter cartConverter;

    @Autowired
    private CustomerService customerService;

    private Specification<CartModel> buildSpecification(CartCriteria criteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new CartSpecificationBuilder(root, criteriaBuilder) //
                .setUserId(criteria.getUserId()) //
                .setProductId(criteria.getProductId()) //
                .build(); //
    }

    @Override
    public Page<CartDTO> findByCriteria(CartCriteria cartCriteria, Long customerId) {
        Pageable pageable = buildPageable(cartCriteria);
        Specification<CartModel> specification = buildSpecification(cartCriteria);
        Page<CartModel> page = cartRepository.findAll(specification, pageable);
        List<CartDTO> cartDTOs = cartConverter.toDTOs(page.getContent(), customerId);

        log.info("------------------ Find by criteria - END ----------------");
        return new PageImpl<>(cartDTOs, pageable, page.getTotalElements());
    }

    @Override
    public CartDTO create(@Valid CartDTO cartDTO, Long customerId) {
        CartModel cartModel = cartRepository.findByUserIdAndNhanhVnProductId(cartDTO.getUserId(), cartDTO.getNhanhVnProductId());
        if (cartModel != null) {
            cartModel.setQuantity(cartModel.getQuantity() + cartDTO.getQuantity());
        } else {
            cartModel = cartConverter.toModel(cartDTO);
            if (cartModel.getQuantity() == 0) {
                cartModel.setQuantity(1);
            }
        }

        cartRepository.save(cartModel);
        log.info("------------------ Create - END ----------------");
        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        return cartConverter.toDTO(cartModel, isNewCustomer);
    }

    @Override
    public CartDTO update(CartModel cartModel, CartDTO cartDTO, Long customerId) {
        if (cartDTO.getQuantity() <= 0) {
            cartRepository.delete(cartModel);
            log.info("------------------ update - END ----------------");
            return null;
        } else {
            cartModel.setQuantity(cartDTO.getQuantity());
            cartRepository.save(cartModel);
            log.info("------------------ update - END ----------------");

            boolean isNewCustomer = false;
            if (customerId != null) {
                isNewCustomer = customerService.isNewCustomer(customerId);
            }
            return cartConverter.toDTO(cartModel, isNewCustomer);
        }
    }

    @Override
    public List<Object[]> findQuantityAndProductByCartIds(List<Long> cartIds) {
        return cartRepository.findQuantityAndProductByCartIds(cartIds);
    }

    @Override
    public List<CartModel> findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public List<CartProduct> findQuantityAndProductByCartIdIn(List<Long> cartIds) {
        return cartRepository.findQuantityAndProductByCartIdIn(cartIds);
    }

    @Override
    public void updatePreSelectFalseByIdIn(List<Long> cartIds) {
        cartRepository.updatePreSelectFalseByIdIn(cartIds);
    }
}
