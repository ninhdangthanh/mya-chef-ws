//package vn.com.ids.mammy.business.converter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import vn.com.ids.mammy.dao.model.OrderDetailModel;
//
//@Component
//public class OrderDetailConverter {
//
//    @Autowired
//    private ModelMapper mapper;
//	
//	
//
//    public OrderDetailDTO toBasicDTO(OrderDetailModel orderDetailModel) {
//        return mapper.map(orderDetailModel, OrderDetailDTO.class);
//    }
//
//    public List<OrderDetailDTO> toBasicDTOs(List<OrderDetailModel> orderDetailModels) {
//        if (CollectionUtils.isEmpty(orderDetailModels)) {
//            return new ArrayList<>();
//        }
//
//        List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
//        for (OrderDetailModel orderDetailModel : orderDetailModels) {
//            orderDetailDTOs.add(toBasicDTO(orderDetailModel));
//        }
//
//        return orderDetailDTOs;
//    }
//
//    public OrderDetailDTO toDTO(OrderDetailModel orderDetailModel) {
//        return toBasicDTO(orderDetailModel);
//    }
//
//    public List<OrderDetailDTO> toDTOs(List<OrderDetailModel> orderDetailModels) {
//        if (CollectionUtils.isEmpty(orderDetailModels)) {
//            return new ArrayList<>();
//        }
//
//        List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
//        for (OrderDetailModel orderDetailModel : orderDetailModels) {
//            orderDetailDTOs.add(toDTO(orderDetailModel));
//        }
//
//        return orderDetailDTOs;
//    }
//	
//	public OrderDetailModel toModel(OrderDetailDTO orderDetailDTO) {
//        OrderDetailModel orderDetailModel = mapper.map(orderDetailDTO, OrderDetailModel.class);
//        return orderDetailModel;
//    }
//	
//	
//}

