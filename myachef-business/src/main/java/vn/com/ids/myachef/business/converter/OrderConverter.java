package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.OrderDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.model.OrderModel;

@Component
public class OrderConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationConfig applicationConfig;

    public OrderDTO toBasicDTO(OrderModel orderModel) {
        OrderDTO orderDTO = mapper.map(orderModel, OrderDTO.class);
        if (StringUtils.hasText(orderDTO.getImagePayment()) && !orderDTO.getImagePayment().startsWith("https://pos.nvncdn.net")) {
            orderDTO.setImagePayment(fileUploadService.getFilePath(applicationConfig.getOrderPath(), orderModel.getImagePayment()));
        }
        return orderDTO;
    }

    public OrderModel toBasicModel(OrderDTO orderDTO) {
        return mapper.map(orderDTO, OrderModel.class);
    }

    public List<OrderDTO> toBasicDTOs(List<OrderModel> orderModels) {
        List<OrderDTO> orderDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderModels)) {
            for (OrderModel orderModel : orderModels) {
                orderDTOs.add(toBasicDTO(orderModel));
            }
        }
        return orderDTOs;
    }

    public void mapDataToUpdate(OrderModel model, OrderDTO dto) {
        if (dto.getTotalPayment() != null) {
            model.setTotalPayment(dto.getTotalPayment());
        }
        if (dto.getStatus() != null) {
            model.setStatus(dto.getStatus());
        }
    }
}
