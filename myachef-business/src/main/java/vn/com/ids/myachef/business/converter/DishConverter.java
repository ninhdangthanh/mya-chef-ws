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
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.enums.DishStatus;
import vn.com.ids.myachef.dao.model.DishCategoryModel;
import vn.com.ids.myachef.dao.model.DishModel;

@Component
public class DishConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationConfig applicationConfig;

    public DishDTO toBasicDTO(DishModel dishModel) {
        DishDTO dishDTO = mapper.map(dishModel, DishDTO.class);
        if (StringUtils.hasText(dishModel.getImage()) && !dishModel.getImage().startsWith("https://pos.nvncdn.net")) {
            dishDTO.setImage(fileUploadService.getFilePath(applicationConfig.getDishPath(), dishModel.getImage()));
        }
        return dishDTO;
    }

    public DishModel toBasicModel(DishDTO dishDTO) {
        return mapper.map(dishDTO, DishModel.class);
    }

    public List<DishDTO> toBasicDTOs(List<DishModel> dishModels) {
        List<DishDTO> dishDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dishModels)) {
            for (DishModel ingredientModel : dishModels) {
                dishDTOs.add(toBasicDTO(ingredientModel));
            }
        }
        return dishDTOs;
    }

    public DishDTO toDTO(DishModel dishModel) {
        DishDTO dishDTO = toBasicDTO(dishModel);
        return dishDTO;
    }

    public void mapDataToUpdate(DishModel model, DishDTO dto) {
        if (StringUtils.hasText(dto.getName())) {
            model.setName(dto.getName());
        }
        if (StringUtils.hasText(dto.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            model.setStatus(dto.getStatus());
        }
        if (dto.getDishStatus() != null) {
            model.setDishStatus(dto.getDishStatus());
        }
        if (dto.getPrice() != null && dto.getPrice() >= 0) {
            model.setPrice(dto.getPrice());
        }
        if (StringUtils.hasText(dto.getPriceLabel())) {
            model.setPriceLabel(dto.getPriceLabel());
        }
    }
}
