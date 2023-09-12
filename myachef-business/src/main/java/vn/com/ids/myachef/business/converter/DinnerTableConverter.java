package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.DinnerTableDTO;
import vn.com.ids.myachef.business.dto.IngredientCategoryDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.model.DinnerTableModel;
import vn.com.ids.myachef.dao.model.IngredientCategoryModel;
import vn.com.ids.myachef.dao.model.IngredientModel;

@Component
public class DinnerTableConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationConfig applicationConfig;

    public DinnerTableDTO toBasicDTO(DinnerTableModel dinnerTableModel) {
        DinnerTableDTO dinnerTableDTO = mapper.map(dinnerTableModel, DinnerTableDTO.class);
        return dinnerTableDTO;
    }

    public DinnerTableModel toBasicModel(DinnerTableDTO ingredientCategoryDTO) {
        return mapper.map(ingredientCategoryDTO, DinnerTableModel.class);
    }

    public List<DinnerTableDTO> toBasicDTOs(List<DinnerTableModel> dinnerTableModels) {
        List<DinnerTableDTO> categoryDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dinnerTableModels)) {
            for (DinnerTableModel dinnerTableModel : dinnerTableModels) {
                categoryDTOs.add(toBasicDTO(dinnerTableModel));
            }
        }
        return categoryDTOs;
    }
    
    public DinnerTableDTO toDTO(DinnerTableModel dinnerTableModel) {
        DinnerTableDTO dinnerTableDTO = toBasicDTO(dinnerTableModel);
        
        return dinnerTableDTO;
    }

//    public IngredientCategoryDTO toDTO(IngredientCategoryModel ingredientCategoryModel) {
//        IngredientCategoryDTO ingredientCategoryDTO = toBasicDTO(ingredientCategoryModel);
//        if (!CollectionUtils.isEmpty(ingredientCategoryModel.getIngredients())) {
//            for (IngredientModel ingredient : ingredientCategoryModel.getIngredients()) {
//                ingredientCategoryDTO.getIngredientDTOs().add(ingredientConverter.toBasicDTO(ingredient));
//            }
//        }
//        return ingredientCategoryDTO;
//    }

    public void mapDataToUpdate(DinnerTableModel model, DinnerTableDTO dto) {
        if (StringUtils.hasText(dto.getTableNumber())) {
            model.setTableNumber(dto.getTableNumber());
        }
        if (dto.getStatus() != null) {
            model.setStatus(dto.getStatus());
        }
    }
}
