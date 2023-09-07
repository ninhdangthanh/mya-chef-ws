package vn.com.ids.myachef.business.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.service.IngredientService;
import vn.com.ids.myachef.dao.model.IngredientModel;
import vn.com.ids.myachef.dao.repository.IngredientRepository;

@Service
@Transactional
public class IngredientServiceImpl extends AbstractService<IngredientModel, Long> implements IngredientService {

	private IngredientRepository ingredientRepository;

	protected IngredientServiceImpl(IngredientRepository ingredientRepository) {
		super(ingredientRepository);
		this.ingredientRepository = ingredientRepository;
	}

}
