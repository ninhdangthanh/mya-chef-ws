package vn.com.ids.myachef.business.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.ids.myachef.business.service.DishService;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.repository.DishRepository;

@Service
@Transactional
public class DishServiceImpl extends AbstractService<DishModel, Long> implements DishService {

	private DishRepository dishRepository;

	protected DishServiceImpl(DishRepository dishRepository) {
		super(dishRepository);
		this.dishRepository = dishRepository;
	}

}
