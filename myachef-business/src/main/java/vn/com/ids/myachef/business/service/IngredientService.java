package vn.com.ids.myachef.business.service;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.IngredientDTO;
import vn.com.ids.myachef.dao.criteria.IngredientCriteria;
import vn.com.ids.myachef.dao.model.IngredientModel;

public interface IngredientService extends IGenericService<IngredientModel, Long> {
	
	IngredientDTO create(@Valid IngredientDTO ingredientDTO, MultipartFile image);
	
	IngredientDTO update(IngredientDTO ingredientDTO, IngredientModel ingredientModel, MultipartFile image);

	List<IngredientDTO> manualAddIngredient(@Valid List<IngredientDTO> ingredientDTOs);

    Page<IngredientModel> findAll(IngredientCriteria ingredientCriteria);

    List<IngredientModel> findByIdIn(Set<Long> ids);
}
