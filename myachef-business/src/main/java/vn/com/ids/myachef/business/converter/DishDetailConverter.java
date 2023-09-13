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
import vn.com.ids.myachef.business.dto.DishDetailDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.enums.DishStatus;
import vn.com.ids.myachef.dao.model.DishCategoryModel;
import vn.com.ids.myachef.dao.model.DishDetailModel;
import vn.com.ids.myachef.dao.model.DishModel;

@Component
public class DishDetailConverter {

    @Autowired
    private ModelMapper mapper;

    public DishDetailDTO toBasicDTO(DishDetailModel dishDetailModel) {
        DishDetailDTO dishDetailDTO = mapper.map(dishDetailModel, DishDetailDTO.class);
        return dishDetailDTO;
    }

    public DishDetailModel toBasicModel(DishDetailDTO dishDetailDTO) {
        return mapper.map(dishDetailDTO, DishDetailModel.class);
    }

    public List<DishDetailDTO> toBasicDTOs(List<DishDetailModel> dishDetailModels) {
        List<DishDetailDTO> dishDetailDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dishDetailModels)) {
            for (DishDetailModel ingredientModel : dishDetailModels) {
                dishDetailDTOs.add(toBasicDTO(ingredientModel));
            }
        }
        return dishDetailDTOs;
    }

    public DishDetailDTO toDTO(DishDetailModel dishDetailModel) {
        DishDetailDTO dishDTO = toBasicDTO(dishDetailModel);
        return dishDTO;
    }

    public void mapDataToUpdate(DishDetailModel model, DishDetailDTO dto) {
        if (dto.getQuantity() != null && dto.getQuantity() > 0) {
            model.setQuantity(dto.getQuantity());
        }
        if (dto.getStatus() != null) {
            model.setStatus(dto.getStatus());
        }
    }
}
