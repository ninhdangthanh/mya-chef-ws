package vn.com.ids.myachef.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.DishConverter;
import vn.com.ids.myachef.business.dto.DishDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.DishCategoryService;
import vn.com.ids.myachef.business.service.DishService;
import vn.com.ids.myachef.business.service.IngredientService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.criteria.DishCriteria;
import vn.com.ids.myachef.dao.criteria.builder.DishSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.DishStatus;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.DishCategoryModel;
import vn.com.ids.myachef.dao.model.DishDetailModel;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.model.IngredientModel;
import vn.com.ids.myachef.dao.repository.DishDetailRepository;
import vn.com.ids.myachef.dao.repository.DishRepository;

@Service
@Transactional
public class DishServiceImpl extends AbstractService<DishModel, Long> implements DishService {

    private DishRepository dishRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private DishConverter dishConverter;
    
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private DishCategoryService dishCategoryService;
    
    @Autowired
    private DishDetailRepository dishDetailRepository;

    protected DishServiceImpl(DishRepository dishRepository) {
        super(dishRepository);
        this.dishRepository = dishRepository;
    }

    public Specification<DishModel> buildSpecification(DishCriteria dishCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new DishSpecificationBuilder(root, criteriaBuilder) //
                .setName(dishCriteria.getName())//
                .setImage(dishCriteria.getImage()) //
                .setPrice(dishCriteria.getPrice()) //
                .setPriceLabel(dishCriteria.getPriceLabel()).setDescription(dishCriteria.getDescription()) //
                .setStatus(dishCriteria.getStatus()).setDishStatus(dishCriteria.getDiskStatus()) //
                .build();
    }

    @Override
    public Page<DishModel> findAll(DishCriteria dishCriteria) {
        Specification<DishModel> specification = buildSpecification(dishCriteria);
        Pageable pageable = buildPageable(dishCriteria);
        return dishRepository.findAll(specification, pageable);
    }

    @Override
    public DishDTO create(@Valid DishDTO dishDTO, MultipartFile image) {
        DishModel dishModel = dishConverter.toBasicModel(dishDTO);
        if (!StringUtils.hasText(dishModel.getName()) || dishModel.getPrice() == null || dishModel.getPrice() <= 0
                || !StringUtils.hasText(dishModel.getPriceLabel())) {
            throw new BadRequestException("Field name, price, price label can not blank or null or <= 0");
        }

        if (dishDTO.getDishCategoryId() != null) {
            DishCategoryModel dishCategoryModel = dishCategoryService.findOne(dishDTO.getDishCategoryId());
            if (dishCategoryModel == null) {
                throw new ResourceNotFoundException("Not found dish category with id: " + dishDTO.getDishCategoryId());
            }
            dishModel.setDishCategory(dishCategoryModel);
        }

        if (image != null) {
            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullDishPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullDishPath(), prefixName), generatedName, image);
            dishModel.setImage(generatedName);
        }
        
        boolean avalable = true;
        List<DishDetailModel> dishDetailModels = new ArrayList<>();
        if(dishDTO.getDishDetailHashMap().keySet() != null) {
            Map<Long, Double> dishDetailHashMap = dishDTO.getDishDetailHashMap();
            List<IngredientModel> ingredientModels = ingredientService.findByIdIn(dishDetailHashMap.keySet());
            Map<Long, IngredientModel> ingredientModelHashMaps = ingredientModels.stream().collect(Collectors.toMap(IngredientModel::getId, Function.identity()));
            
            for(Long ingredientId : dishDetailHashMap.keySet()) {
                 IngredientModel ingredientModel = ingredientModelHashMaps.get(ingredientId);
                 if(ingredientModel == null) {
                     continue;
                 }
                 DishDetailModel dishDetailModel = new DishDetailModel();
                 dishDetailModel.setIngredient(ingredientModel);
                 dishDetailModel.setStatus(Status.ACTIVE);
                 if(dishDetailHashMap.get(ingredientId) == null || 
                         dishDetailHashMap.get(ingredientId) <= 0) {
                     dishDetailModel.setQuantity(0.0);
                 } else {
                     dishDetailModel.setQuantity(dishDetailHashMap.get(ingredientId));
                     if(dishDetailHashMap.get(ingredientId) > ingredientModel.getQuantity()) {
                         avalable = false;
                     }
                 }
                 dishDetailModel.setDish(dishModel);
                 dishDetailModels.add(dishDetailModel);
            }
            dishModel.setDishDetails(dishDetailModels);
        }
        
        dishModel.setStatus(Status.ACTIVE);
        if(avalable) {
            dishModel.setDishStatus(DishStatus.AVAILABLE);
        } else {
            dishModel.setDishStatus(DishStatus.NOT_ENOUGH_INGREDIENTS);
        }
        dishModel = save(dishModel);
        
        return dishConverter.toBasicDTO(dishModel);
    }

    @Override
    public DishDTO update(@Valid DishDTO dishDTO, DishModel dishModel, MultipartFile image) {
//     // update dish detail -start
//        List<DishDetailModel> currentDishDetailModels = dishDetailRepository.findByDishId(dishModel.getId());
//        if(currentDishDetailModels != null) {
//            dishDetailRepository.deleteByDishId(dishModel.getId());
//        }
//        boolean avalable = true;
//        List<DishDetailModel> dishDetailModels = new ArrayList<>();
//        if(dishDTO.getDishDetailHashMap().keySet() != null) {
//            Map<Long, Double> dishDetailHashMap = dishDTO.getDishDetailHashMap();
//            List<IngredientModel> ingredientModels = ingredientService.findByIdIn(dishDetailHashMap.keySet());
//            Map<Long, IngredientModel> ingredientModelHashMaps = ingredientModels.stream().collect(Collectors.toMap(IngredientModel::getId, Function.identity()));
//            
//            for(Long ingredientId : dishDetailHashMap.keySet()) {
//                 IngredientModel ingredientModel = ingredientModelHashMaps.get(ingredientId);
//                 if(ingredientModel == null) {
//                     continue;
//                 }
//                 DishDetailModel dishDetailModel = new DishDetailModel();
//                 dishDetailModel.setIngredient(ingredientModel);
//                 dishDetailModel.setStatus(Status.ACTIVE);
//                 if(dishDetailHashMap.get(ingredientId) == null || 
//                         dishDetailHashMap.get(ingredientId) <= 0) {
//                     dishDetailModel.setQuantity(0.0);
//                 } else {
//                     dishDetailModel.setQuantity(dishDetailHashMap.get(ingredientId));
//                     if(dishDetailHashMap.get(ingredientId) > ingredientModel.getQuantity()) {
//                         avalable = false;
//                     }
//                 }
//                 dishDetailModel.setDish(dishModel);
//                 dishDetailModels.add(dishDetailModel);
//            }
//            dishModel.setDishDetails(dishDetailModels);
//        }
//            if(avalable) {
//                dishModel.setDishStatus(DishStatus.AVAILABLE);
//            } else {
//                dishModel.setDishStatus(DishStatus.NOT_ENOUGH_INGREDIENTS);
//            }
//        // update dish detail -end
        
        if (image != null) {
            if (StringUtils.hasText(dishModel.getImage())) {
                fileStorageService.delete(applicationConfig.getFullDishPath() + dishModel.getImage());
            }

            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullDishPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullDishPath(), prefixName), generatedName, image);
            dishModel.setImage(generatedName);
        }

        dishConverter.mapDataToUpdate(dishModel, dishDTO);

        if (dishDTO.getDishCategoryId() != null) {
            DishCategoryModel dishCategoryModel = dishCategoryService.findOne(dishDTO.getDishCategoryId());
            if (dishCategoryModel == null) {
                throw new ResourceNotFoundException("Not found dish category with id: " + dishDTO.getDishCategoryId());
            }
            dishModel.setDishCategory(dishCategoryModel);
        }
        
        dishModel.setStatus(Status.ACTIVE);

        dishModel = save(dishModel);

        return dishConverter.toBasicDTO(dishModel);
    }

    public List<DishModel> findByDishCategoryId(Long id) {
        return dishRepository.findByDishCategoryId(id);
    }

}
