package vn.com.ids.myachef.business.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.DishDTO;
import vn.com.ids.myachef.dao.criteria.DishCriteria;
import vn.com.ids.myachef.dao.model.DishModel;

public interface DishService extends IGenericService<DishModel, Long> {

    Page<DishModel> findAll(DishCriteria dishCriteria);

    DishDTO create(@Valid DishDTO dishDTO, MultipartFile image);

    DishDTO update(@Valid DishDTO dishDTO, DishModel dishModel, MultipartFile image);

}
