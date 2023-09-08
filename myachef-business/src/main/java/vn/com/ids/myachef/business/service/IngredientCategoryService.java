package vn.com.ids.myachef.business.service;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.IngredientCategoryDTO;
import vn.com.ids.myachef.dao.model.IngredientCategoryModel;

public interface IngredientCategoryService extends IGenericService<IngredientCategoryModel, Long> {

	IngredientCategoryDTO create(@Valid IngredientCategoryDTO ingredientCategoryDTO, MultipartFile image);
	
	IngredientCategoryDTO update(IngredientCategoryDTO ingredientCategoryDTO, IngredientCategoryModel ingredientCategoryModel, MultipartFile image);

}
