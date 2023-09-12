package vn.com.ids.myachef.business.service;

import javax.validation.Valid;

import vn.com.ids.myachef.business.dto.DinnerTableDTO;
import vn.com.ids.myachef.dao.model.DinnerTableModel;

public interface DinnerTableService extends IGenericService<DinnerTableModel, Long> {

    DinnerTableDTO create(@Valid DinnerTableDTO dinnerTableDTO);

    DinnerTableDTO update(@Valid DinnerTableDTO dinnerTableDTO, DinnerTableModel dinnerTableModel);

}
