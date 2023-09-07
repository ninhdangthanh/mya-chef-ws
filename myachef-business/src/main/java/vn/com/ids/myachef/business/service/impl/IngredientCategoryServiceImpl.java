package vn.com.ids.myachef.business.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.service.IngredientCategoryService;
import vn.com.ids.myachef.dao.model.IngredientCategoryModel;
import vn.com.ids.myachef.dao.repository.IngredientCategoryRepository;

@Service
@Transactional
public class IngredientCategoryServiceImpl extends AbstractService<IngredientCategoryModel, Long>
		implements IngredientCategoryService {

	private IngredientCategoryRepository ingredientCategoryRepository;

	protected IngredientCategoryServiceImpl(IngredientCategoryRepository ingredientCategoryRepository) {
		super(ingredientCategoryRepository);
		this.ingredientCategoryRepository = ingredientCategoryRepository;
	}

}
