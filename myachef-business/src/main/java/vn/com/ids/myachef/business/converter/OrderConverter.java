package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.OrderDTO;
import vn.com.ids.myachef.business.dto.ProductConfigDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.model.OrderModel;

@Component
public class OrderConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerConverter customerConverter;

    @Autowired
    private ProductConfigConverter productConfigConverter;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationConfig applicationConfig;

    public OrderDTO toBasicDTO(OrderModel orderModel, HttpServletRequest request) {
        OrderDTO orderDTO = mapper.map(orderModel, OrderDTO.class);
        // if (orderModel.getTotalPayment() != 0) {
        // orderDTO.setTotalPayment(orderModel.getTotalPayment());
        // }
        orderDTO.setPaymentImage(
                fileUploadService.getFilePath(String.format(applicationConfig.getOrderPath(), orderModel.getId()), orderModel.getPaymentImage(), request));
        return orderDTO;
    }

    public List<OrderDTO> toBasicDTOs(List<OrderModel> orderModels, HttpServletRequest request) {
        List<OrderDTO> orderDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderModels)) {
            for (OrderModel orderModel : orderModels) {
                orderDTOs.add(toBasicDTO(orderModel, request));
            }
        }

        return orderDTOs;
    }

    public OrderDTO toDTO(OrderModel orderModel, HttpServletRequest request) {
        if (orderModel == null) {
            return null;
        }
        OrderDTO orderDTO = toBasicDTO(orderModel, request);
        // if (!CollectionUtils.isEmpty(orderModel.getOrderDetails())) {
        orderDTO.setProductDTOs(addProducts(orderModel));
        // }
        orderDTO.setCustomerDTO(customerConverter.toBasicDTO(orderModel.getCustomer()));

        return orderDTO;
    }

    private List<ProductConfigDTO> addProducts(OrderModel orderModel) {
        List<ProductConfigDTO> productDTOs = new ArrayList<>();
        for (OrderDetailModel orderDetailModel : orderModel.getOrderDetails()) {
            ProductConfigDTO productDTO = productConfigConverter.toBasicDTO(orderDetailModel.getProduct(), false);
            productDTO.setOrderQuantity(orderDetailModel.getQuantity());
            productDTO.setOrderTotalPrice(orderDetailModel.getTotalProductPrice());
            productDTO.setPrice(orderDetailModel.getProductCurrentPrice());
            productDTOs.add(productDTO);
        }
        return productDTOs;
    }

    public List<OrderDTO> toDTOs(List<OrderModel> orderModels, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(orderModels)) {
            return new ArrayList<>();
        }

        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (OrderModel orderModel : orderModels) {
            OrderDTO orderDTO = toBasicDTO(orderModel, request);
            orderDTO.setProductDTOs(addProducts(orderModel));
            orderDTOs.add(orderDTO);
        }

        return orderDTOs;
    }

    public OrderModel toModel(OrderDTO orderDTO) {
        return mapper.map(orderDTO, OrderModel.class);
    }

    // public void addOrderDetailDTOs(OrderModel orderModel, OrderDTO orderDTO) {
    // if (!CollectionUtils.isEmpty(orderModel.getOrderDetails())) {
    // orderDTO.setOrderDetailDTOs(orderDetailConverter.toBasicDTOs(orderModel.getOrderDetails()));
    // }
    // }

}
