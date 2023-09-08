package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.IngredientCategoryDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.model.IngredientCategoryModel;
import vn.com.ids.myachef.dao.model.IngredientModel;

@Component
public class IngredientCategoryConverter {
	
	@Autowired
    private ModelMapper mapper;
	
	@Autowired
	private IngredientConverter ingredientConverter;
	
	@Autowired
    private FileUploadService fileUploadService;
	
	@Autowired
    private ApplicationConfig applicationConfig;
	
	public IngredientCategoryDTO toBasicDTO(IngredientCategoryModel ingredientCategoryModel) {
		IngredientCategoryDTO ingredientCategoryDTO = mapper.map(ingredientCategoryModel, IngredientCategoryDTO.class);
		if (StringUtils.hasText(ingredientCategoryDTO.getImage()) && !ingredientCategoryDTO.getImage().startsWith("https://pos.nvncdn.net")) {
			ingredientCategoryDTO
                    .setImage(fileUploadService.getFilePath(applicationConfig.getIngredientCategoryPath(), ingredientCategoryModel.getImage()));
        }
		return ingredientCategoryDTO;
	}
	
	public IngredientCategoryModel toBasicModel(IngredientCategoryDTO ingredientCategoryDTO) {
		return mapper.map(ingredientCategoryDTO, IngredientCategoryModel.class);
	}
	
	public List<IngredientCategoryDTO> toBasicDTOs(List<IngredientCategoryModel> ingredientCategoryModels) {
		List<IngredientCategoryDTO> categoryDTOs = new ArrayList<>();
		if(!CollectionUtils.isEmpty(ingredientCategoryModels)) {
			for (IngredientCategoryModel ingredientCategoryModel : ingredientCategoryModels) {
				categoryDTOs.add(toBasicDTO(ingredientCategoryModel));
			}
		}
		return categoryDTOs;
	}
	
	public IngredientCategoryDTO toDTO(IngredientCategoryModel ingredientCategoryModel) {
		IngredientCategoryDTO ingredientCategoryDTO = toBasicDTO(ingredientCategoryModel);
		if(!CollectionUtils.isEmpty(ingredientCategoryModel.getIngredients())) {
			for (IngredientModel ingredient : ingredientCategoryModel.getIngredients()) {
				ingredientCategoryDTO.getIngredientDTOs().add(ingredientConverter.toBasicDTO(ingredient));
			}
		}
		return ingredientCategoryDTO;
	}
	
	public void mapDataToUpdate(IngredientCategoryModel model, IngredientCategoryDTO dto) {
        if (StringUtils.hasText(dto.getName())) {
            model.setName(dto.getName());
        }
        if (StringUtils.hasText(dto.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            model.setStatus(dto.getStatus());
        }
    }
}
