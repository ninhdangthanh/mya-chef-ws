package vn.com.ids.myachef.business.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.service.DinnerTableService;
import vn.com.ids.myachef.dao.model.DinnerTableModel;
import vn.com.ids.myachef.dao.repository.DinnerTableRepository;

@Service
@Transactional
public class DinnerTableServiceImpl extends AbstractService<DinnerTableModel, Long> implements DinnerTableService {

	private DinnerTableRepository dinnerTableRepository;

	protected DinnerTableServiceImpl(DinnerTableRepository dinnerTableRepository) {
		super(dinnerTableRepository);
		this.dinnerTableRepository = dinnerTableRepository;
	}

}
