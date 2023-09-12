package vn.com.ids.myachef.business.service;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.DishCategoryDTO;
import vn.com.ids.myachef.business.dto.IngredientCategoryDTO;
import vn.com.ids.myachef.dao.model.DishCategoryModel;

public interface DishCategoryService extends IGenericService<DishCategoryModel, Long> {

    DishCategoryDTO create(@Valid DishCategoryDTO dishCategoryDTO, MultipartFile image);

    DishCategoryDTO update(@Valid IngredientCategoryDTO ingredientCategoryDTO, DishCategoryModel dishCategoryModel, MultipartFile image);

}
