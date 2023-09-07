package vn.com.ids.myachef.business.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.service.DishCategoryService;
import vn.com.ids.myachef.dao.model.DishCategoryModel;
import vn.com.ids.myachef.dao.repository.DishCategoryRepository;

@Service
@Transactional
public class DishCategoryServiceImpl extends AbstractService<DishCategoryModel, Long> implements DishCategoryService {

	private DishCategoryRepository dishCategoryRepository;

	protected DishCategoryServiceImpl(DishCategoryRepository dishCategoryRepository) {
		super(dishCategoryRepository);
		this.dishCategoryRepository = dishCategoryRepository;
	}

}
