package vn.com.ids.myachef.business.service.impl;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.DishCategoryConverter;
import vn.com.ids.myachef.business.dto.DishCategoryDTO;
import vn.com.ids.myachef.business.dto.IngredientCategoryDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.service.DishCategoryService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.model.DishCategoryModel;
import vn.com.ids.myachef.dao.repository.DishCategoryRepository;

@Service
@Transactional
public class DishCategoryServiceImpl extends AbstractService<DishCategoryModel, Long> implements DishCategoryService {

	private DishCategoryRepository dishCategoryRepository;
	
	@Autowired
	private DishCategoryConverter dishCategoryConverter;
	
	@Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private ApplicationConfig applicationConfig;

	protected DishCategoryServiceImpl(DishCategoryRepository dishCategoryRepository) {
		super(dishCategoryRepository);
		this.dishCategoryRepository = dishCategoryRepository;
	}

    @Override
    public DishCategoryDTO create(@Valid DishCategoryDTO dishCategoryDTO, MultipartFile image) {
        DishCategoryModel dishCategoryModel = dishCategoryConverter.toBasicModel(dishCategoryDTO);
        if(!StringUtils.hasText(dishCategoryModel.getName())) {
            throw new BadRequestException("Field name can not blank or null");
        }
        if(image != null) {
            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullDishCategoryPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullDishCategoryPath(), prefixName), generatedName,
                    image);
            dishCategoryModel.setImage(generatedName);
        }
        dishCategoryModel = save(dishCategoryModel);
        return dishCategoryConverter.toBasicDTO(dishCategoryModel);
    }

    @Override
    public DishCategoryDTO update(@Valid DishCategoryDTO dishCategoryDTO, DishCategoryModel dishCategoryModel, MultipartFile image) {
        if(image != null) {
            if (StringUtils.hasText(dishCategoryModel.getImage())) {
                fileStorageService.delete(applicationConfig.getFullDishCategoryPath() + dishCategoryModel.getImage());
            }
            
            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullDishCategoryPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullDishCategoryPath(), prefixName), generatedName,
                    image);
            dishCategoryModel.setImage(generatedName);
        }
        
        dishCategoryConverter.mapDataToUpdate(dishCategoryModel, dishCategoryDTO);
        dishCategoryModel = save(dishCategoryModel);
        
        return dishCategoryConverter.toBasicDTO(dishCategoryModel);
    }

}
