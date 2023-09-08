package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.IngredientDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.model.IngredientModel;

@Component
public class IngredientConverter {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
    private FileUploadService fileUploadService;
	
	@Autowired
    private ApplicationConfig applicationConfig;

	public IngredientDTO toBasicDTO(IngredientModel ingredientModel) {
		IngredientDTO ingredientDTO = mapper.map(ingredientModel, IngredientDTO.class);
		if (StringUtils.hasText(ingredientDTO.getImage()) && !ingredientDTO.getImage().startsWith("https://pos.nvncdn.net")) {
			ingredientDTO
                    .setImage(fileUploadService.getFilePath(applicationConfig.getIngredientPath(), ingredientModel.getImage()));
        }
		return ingredientDTO;
	}
	
	public IngredientModel toBasicModel(IngredientDTO ingredientDTO) {
		return mapper.map(ingredientDTO, IngredientModel.class);
	}
	
	public List<IngredientDTO> toBasicDTOs(List<IngredientModel> ingredientModels) {
		List<IngredientDTO> ingredientDTOs = new ArrayList<>();
		if(!CollectionUtils.isEmpty(ingredientModels)) {
			for (IngredientModel ingredientModel : ingredientModels) {
				ingredientDTOs.add(toBasicDTO(ingredientModel));
			}
		}
		return ingredientDTOs;
	}
	
	public void mapDataToUpdate(IngredientModel model, IngredientDTO dto) {
        if (StringUtils.hasText(dto.getName())) {
            model.setName(dto.getName());
        }
        if (StringUtils.hasText(dto.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            model.setStatus(dto.getStatus());
        }
        if (dto.getPrice() != null && dto.getPrice() > 0) {
        	model.setPrice(dto.getPrice());
        }
        if (dto.getQuantity() != null && dto.getQuantity() > 0) {
        	model.setQuantity(dto.getQuantity());
        }
        if (StringUtils.hasText(dto.getUnit())) {
            model.setUnit(dto.getUnit());
        }
    }
}
