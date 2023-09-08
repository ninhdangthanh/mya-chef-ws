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
import vn.com.ids.myachef.business.converter.IngredientCategoryConverter;
import vn.com.ids.myachef.business.dto.IngredientCategoryDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.service.IngredientCategoryService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.IngredientCategoryModel;
import vn.com.ids.myachef.dao.repository.IngredientCategoryRepository;

@Service
@Transactional
public class IngredientCategoryServiceImpl extends AbstractService<IngredientCategoryModel, Long>
		implements IngredientCategoryService {

	private IngredientCategoryRepository ingredientCategoryRepository;
	
	@Autowired
	private IngredientCategoryConverter ingredientCategoryConverter;
	
	@Autowired
    private FileStorageService fileStorageService;
	
	@Autowired
    private ApplicationConfig applicationConfig;

	protected IngredientCategoryServiceImpl(IngredientCategoryRepository ingredientCategoryRepository) {
		super(ingredientCategoryRepository);
		this.ingredientCategoryRepository = ingredientCategoryRepository;
	}

	@Override
	public IngredientCategoryDTO create(@Valid IngredientCategoryDTO ingredientCategoryDTO, MultipartFile image) {
		IngredientCategoryModel ingredientCategoryModel = ingredientCategoryConverter.toBasicModel(ingredientCategoryDTO);
		if(!StringUtils.hasText(ingredientCategoryModel.getName())) {
			throw new BadRequestException("Field name can not blank or null");
		}
		if(image != null) {
			String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullIngredientCategoryPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullIngredientCategoryPath(), prefixName), generatedName,
                    image);
            ingredientCategoryModel.setImage(generatedName);
		}
		ingredientCategoryModel.setStatus(Status.ACTIVE);
		ingredientCategoryModel = save(ingredientCategoryModel);
		return ingredientCategoryConverter.toBasicDTO(ingredientCategoryModel);
	}
	
	@Override
	public IngredientCategoryDTO update(IngredientCategoryDTO ingredientCategoryDTO, IngredientCategoryModel ingredientCategoryModel, MultipartFile image) {
		if(image != null) {
			if (StringUtils.hasText(ingredientCategoryModel.getImage())) {
                fileStorageService.delete(applicationConfig.getFullIngredientCategoryPath() + ingredientCategoryModel.getImage());
            }
			
			String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullIngredientCategoryPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullIngredientCategoryPath(), prefixName), generatedName,
                    image);
            ingredientCategoryModel.setImage(generatedName);
		}
		
		ingredientCategoryConverter.mapDataToUpdate(ingredientCategoryModel, ingredientCategoryDTO);
		ingredientCategoryModel = save(ingredientCategoryModel);
		
		return ingredientCategoryConverter.toBasicDTO(ingredientCategoryModel);
	}

}
