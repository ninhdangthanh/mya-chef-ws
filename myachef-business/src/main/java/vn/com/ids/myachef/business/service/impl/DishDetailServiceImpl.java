package vn.com.ids.myachef.business.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.ids.myachef.business.service.DishDetailService;
import vn.com.ids.myachef.dao.model.DishDetailModel;
import vn.com.ids.myachef.dao.repository.DishDetailRepository;

@Service
@Transactional
public class DishDetailServiceImpl extends AbstractService<DishDetailModel, Long> implements DishDetailService {

	private DishDetailRepository dishDetailRepository;

	protected DishDetailServiceImpl(DishDetailRepository dishDetailRepository) {
		super(dishDetailRepository);
		this.dishDetailRepository = dishDetailRepository;
	}

}
