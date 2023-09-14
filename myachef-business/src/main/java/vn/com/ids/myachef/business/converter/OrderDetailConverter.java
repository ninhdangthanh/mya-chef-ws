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
import vn.com.ids.myachef.business.dto.OrderDetailDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.model.OrderModel;

@Component
public class OrderDetailConverter {

    @Autowired
    private ModelMapper mapper;

    public OrderDetailDTO toBasicDTO(OrderDetailModel orderDetailModel) {
        OrderDetailDTO orderDetailDTO = mapper.map(orderDetailModel, OrderDetailDTO.class);
        return orderDetailDTO;
    }

    public OrderDetailModel toBasicModel(OrderDetailDTO orderDetailDTO) {
        return mapper.map(orderDetailDTO, OrderDetailModel.class);
    }

    public List<OrderDetailDTO> toBasicDTOs(List<OrderDetailModel> orderDetailModels) {
        List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderDetailModels)) {
            for (OrderDetailModel orderDetailModel : orderDetailModels) {
                orderDetailDTOs.add(toBasicDTO(orderDetailModel));
            }
        }
        return orderDetailDTOs;
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
