package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.business.dto.CartDTO;
import vn.com.ids.myachef.business.service.CustomerService;
import vn.com.ids.myachef.business.service.ProductConfigService;
import vn.com.ids.myachef.dao.model.CartModel;
import vn.com.ids.myachef.dao.model.ProductConfigModel;

@Component
public class CartConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProductConfigService productConfigService;

    @Autowired
    private ProductConfigConverter productConfigConverter;

    @Autowired
    private CustomerService customerService;

    public CartDTO toBasicDTO(CartModel cartModel) {
        return mapper.map(cartModel, CartDTO.class);
    }

    public List<CartDTO> toDTOs(List<CartModel> cartModels, Long customerId) {
        boolean isNewCustomer = false;
        if (customerId != null) {
            isNewCustomer = customerService.isNewCustomer(customerId);
        }
        List<CartDTO> cartDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cartModels)) {
            for (CartModel CartModel : cartModels) {
                cartDTOs.add(toDTO(CartModel, isNewCustomer));
            }
        }
        return cartDTOs;
    }

    public CartDTO toDTO(CartModel cartModel, boolean isNewCustomer) {
        CartDTO cartDTO = toBasicDTO(cartModel);
        ProductConfigModel productConfigModel = productConfigService.findByNhanhVnId(cartModel.getNhanhVnProductId());
        cartDTO.setProduct(productConfigConverter.toBasicDTO(productConfigModel, isNewCustomer));
        return cartDTO;
    }

    public CartModel toModel(@Valid CartDTO cartDTO) {
        return mapper.map(cartDTO, CartModel.class);
    }
}
