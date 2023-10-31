package vn.com.ids.myachef.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.ids.myachef.business.converter.DinnerTableConverter;
import vn.com.ids.myachef.business.converter.DishConverter;
import vn.com.ids.myachef.business.dto.DinnerTableDTO;
import vn.com.ids.myachef.business.dto.DishDTO;
import vn.com.ids.myachef.business.service.DinnerTableService;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.DinnerTableModel;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.model.OrderDetailModel;
import vn.com.ids.myachef.dao.model.OrderModel;
import vn.com.ids.myachef.dao.repository.DinnerTableRepository;

@Service
@Transactional
public class DinnerTableServiceImpl extends AbstractService<DinnerTableModel, Long> implements DinnerTableService {

    private DinnerTableRepository dinnerTableRepository;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private DishConverter dishConverter;
    
    @Autowired
    private DinnerTableConverter dinnerTableConverter;

    protected DinnerTableServiceImpl(DinnerTableRepository dinnerTableRepository) {
        super(dinnerTableRepository);
        this.dinnerTableRepository = dinnerTableRepository;
    }

    @Override
    public DinnerTableDTO create(@Valid DinnerTableDTO dinnerTableDTO) {
        DinnerTableModel dinnerTableModel = dinnerTableConverter.toBasicModel(dinnerTableDTO);
        dinnerTableModel.setStatus(Status.ACTIVE);
        dinnerTableModel = save(dinnerTableModel);
        return dinnerTableConverter.toBasicDTO(dinnerTableModel);
    }

    @Override
    public DinnerTableDTO update(@Valid DinnerTableDTO dinnerTableDTO, DinnerTableModel dinnerTableModel) {
        dinnerTableConverter.mapDataToUpdate(dinnerTableModel, dinnerTableDTO);
        dinnerTableModel = save(dinnerTableModel);
        
        return dinnerTableConverter.toBasicDTO(dinnerTableModel);
    }

    @Override
    public List<DishDTO> getDishExistingInDinnerTable(Long dinnerTableId) {
        OrderModel orderModel = orderService.findOrderExistingByDinnerTableId(dinnerTableId);
        List<OrderDetailModel> orderDetailModels = orderModel.getOrderDetails();
        List<DishModel> dishModels = orderDetailModels.stream().map(OrderDetailModel::getDish).collect(Collectors.toList());
        
        return dishConverter.toBasicDTOs(dishModels);
    }

}
