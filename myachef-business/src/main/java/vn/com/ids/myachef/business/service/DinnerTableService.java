package vn.com.ids.myachef.business.service;

import java.util.List;

import javax.validation.Valid;

import vn.com.ids.myachef.business.dto.DinnerTableDTO;
import vn.com.ids.myachef.business.dto.DishDTO;
import vn.com.ids.myachef.dao.model.DinnerTableModel;

public interface DinnerTableService extends IGenericService<DinnerTableModel, Long> {

    DinnerTableDTO create(@Valid DinnerTableDTO dinnerTableDTO);

    DinnerTableDTO update(@Valid DinnerTableDTO dinnerTableDTO, DinnerTableModel dinnerTableModel);

    List<DishDTO> getDishExistingInDinnerTable(Long dinnerTableId);

}
