package vn.com.ids.myachef.business.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.ids.myachef.business.converter.DinnerTableConverter;
import vn.com.ids.myachef.business.dto.DinnerTableDTO;
import vn.com.ids.myachef.business.service.DinnerTableService;
import vn.com.ids.myachef.dao.model.DinnerTableModel;
import vn.com.ids.myachef.dao.repository.DinnerTableRepository;

@Service
@Transactional
public class DinnerTableServiceImpl extends AbstractService<DinnerTableModel, Long> implements DinnerTableService {

    private DinnerTableRepository dinnerTableRepository;
    
    @Autowired
    private DinnerTableConverter dinnerTableConverter;

    protected DinnerTableServiceImpl(DinnerTableRepository dinnerTableRepository) {
        super(dinnerTableRepository);
        this.dinnerTableRepository = dinnerTableRepository;
    }

    @Override
    public DinnerTableDTO create(@Valid DinnerTableDTO dinnerTableDTO) {
        DinnerTableModel dinnerTableModel = dinnerTableConverter.toBasicModel(dinnerTableDTO);
        dinnerTableModel = save(dinnerTableModel);
        return dinnerTableConverter.toBasicDTO(dinnerTableModel);
    }

    @Override
    public DinnerTableDTO update(@Valid DinnerTableDTO dinnerTableDTO, DinnerTableModel dinnerTableModel) {
        dinnerTableConverter.mapDataToUpdate(dinnerTableModel, dinnerTableDTO);
        dinnerTableModel = save(dinnerTableModel);
        
        return dinnerTableConverter.toBasicDTO(dinnerTableModel);
    }

}
