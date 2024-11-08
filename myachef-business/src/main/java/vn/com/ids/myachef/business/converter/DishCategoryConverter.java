package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.DishCategoryDTO;
import vn.com.ids.myachef.business.dto.DishDTO;
import vn.com.ids.myachef.business.dto.IngredientCategoryDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.model.DishCategoryModel;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.model.IngredientCategoryModel;

@Component
public class DishCategoryConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationConfig applicationConfig;
    
    @Autowired
    private DishConverter dishConverter;

    public DishCategoryDTO toBasicDTO(DishCategoryModel dishCategoryModel) {
        DishCategoryDTO dishCategoryDTO = mapper.map(dishCategoryModel, DishCategoryDTO.class);
        if (StringUtils.hasText(dishCategoryModel.getImage()) && !dishCategoryModel.getImage().startsWith("https://pos.nvncdn.net")) {
            dishCategoryDTO
                    .setImage(fileUploadService.getFilePath(applicationConfig.getDishCategoryPath(), dishCategoryModel.getImage()));
        }
        return dishCategoryDTO;
    }

    public DishCategoryModel toBasicModel(DishCategoryDTO dishCategoryDTO) {
        return mapper.map(dishCategoryDTO, DishCategoryModel.class);
    }

    public List<DishCategoryDTO> toBasicDTOs(List<DishCategoryModel> dishCategoryModels) {
        List<DishCategoryDTO> dishCategoryDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dishCategoryModels)) {
            for (DishCategoryModel ingredientCategoryModel : dishCategoryModels) {
                dishCategoryDTOs.add(toBasicDTO(ingredientCategoryModel));
            }
        }
        return dishCategoryDTOs;
    }

    public DishCategoryDTO toDTO(DishCategoryModel dishCategoryModel) {
        DishCategoryDTO dishCategoryDTO = toBasicDTO(dishCategoryModel);
        List<DishDTO> dishDTOs = new ArrayList<>();
        if(!CollectionUtils.isEmpty(dishCategoryModel.getDishs())) {
            for (DishModel dishModel : dishCategoryModel.getDishs()) {
                dishDTOs.add(dishConverter.toBasicDTO(dishModel));
            }
        }
        dishCategoryDTO.setDishDTOs(dishDTOs);
        return dishCategoryDTO;
    }

    public void mapDataToUpdate(DishCategoryModel model, DishCategoryDTO dto) {
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
