package vn.com.ids.myachef.business.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.IngredientConverter;
import vn.com.ids.myachef.business.dto.IngredientDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.IngredientCategoryService;
import vn.com.ids.myachef.business.service.IngredientService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.criteria.IngredientCriteria;
import vn.com.ids.myachef.dao.criteria.builder.IngredientSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.IngredientCategoryModel;
import vn.com.ids.myachef.dao.model.IngredientModel;
import vn.com.ids.myachef.dao.repository.IngredientRepository;

@Service
@Transactional
public class IngredientServiceImpl extends AbstractService<IngredientModel, Long> implements IngredientService {

    private IngredientRepository ingredientRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private IngredientConverter ingredientConverter;

    @Autowired
    private IngredientCategoryService ingredientCategoryService;

    protected IngredientServiceImpl(IngredientRepository ingredientRepository) {
        super(ingredientRepository);
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientDTO create(@Valid IngredientDTO ingredientDTO, MultipartFile image) {
        IngredientModel ingredientModel = ingredientConverter.toBasicModel(ingredientDTO);
        if (!StringUtils.hasText(ingredientModel.getName()) || !StringUtils.hasText(ingredientModel.getUnit())) {
            throw new BadRequestException("Field name and field unit can not blank or null");
        }

        if (ingredientDTO.getIngredientCategoryDTOId() != null) {
            IngredientCategoryModel ingredientCategoryModel = ingredientCategoryService.findOne(ingredientDTO.getIngredientCategoryDTOId());
            if (ingredientCategoryModel == null) {
                throw new ResourceNotFoundException("Not found ingredient category with id: " + ingredientDTO.getIngredientCategoryDTOId());
            }
            ingredientModel.setIngredientCategory(ingredientCategoryModel);
        }

        if (image != null) {
            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullIngredientCategoryPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullIngredientCategoryPath(), prefixName), generatedName, image);
            ingredientModel.setImage(generatedName);
        }

        ingredientModel.setStatus(Status.ACTIVE);
        ingredientModel = save(ingredientModel);

        return ingredientConverter.toBasicDTO(ingredientModel);
    }

    @Override
    public IngredientDTO update(IngredientDTO ingredientDTO, IngredientModel ingredientModel, MultipartFile image) {
        if (image != null) {
            if (StringUtils.hasText(ingredientModel.getImage())) {
                fileStorageService.delete(applicationConfig.getFullIngredientPath() + ingredientModel.getImage());
            }

            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullIngredientPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullIngredientPath(), prefixName), generatedName, image);
            ingredientModel.setImage(generatedName);
        }

        ingredientConverter.mapDataToUpdate(ingredientModel, ingredientDTO);

        if (ingredientDTO.getIngredientCategoryDTOId() != null) {
            IngredientCategoryModel ingredientCategoryModel = ingredientCategoryService.findOne(ingredientDTO.getIngredientCategoryDTOId());
            if (ingredientCategoryModel == null) {
                throw new ResourceNotFoundException("Not found ingredient category with id: " + ingredientDTO.getIngredientCategoryDTOId());
            }
            ingredientModel.setIngredientCategory(ingredientCategoryModel);
        }

        ingredientModel = save(ingredientModel);

        return ingredientConverter.toBasicDTO(ingredientModel);
    }

    @Override
    public List<IngredientDTO> manualAddIngredient(@Valid List<IngredientDTO> ingredientDTOs) {
        Map<Long, Double> mapIngredientDTOs = ingredientDTOs.stream().collect(Collectors.toMap(IngredientDTO::getId, IngredientDTO::getQuantity));
        List<IngredientModel> ingredientModels = ingredientRepository.findAllById(ingredientDTOs.stream().map(i -> i.getId()).collect(Collectors.toList()));

        if (!CollectionUtils.isEmpty(ingredientModels)) {
            for (IngredientModel ingredientModel : ingredientModels) {
                if (mapIngredientDTOs.containsKey(ingredientModel.getId()) && mapIngredientDTOs.get(ingredientModel.getId()) != null
                        && mapIngredientDTOs.get(ingredientModel.getId()) > 0) {
                    Double newQuantity = ingredientModel.getQuantity() + mapIngredientDTOs.get(ingredientModel.getId());
                    ingredientModel.setQuantity(newQuantity);
                }
            }
        }

        ingredientModels = saveAll(ingredientModels);

        return ingredientConverter.toBasicDTOs(ingredientModels);
    }

    @Override
    public Page<IngredientModel> findAll(IngredientCriteria ingredientCriteria) {
        Specification<IngredientModel> specification = buildSpecification(ingredientCriteria);
        Pageable pageable = buildPageable(ingredientCriteria);
        return ingredientRepository.findAll(specification, pageable);
    }
    
    public Specification<IngredientModel> buildSpecification(IngredientCriteria ingredientCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new IngredientSpecificationBuilder(root, criteriaBuilder) //
                .setName(ingredientCriteria.getName())//
                .setPrice(ingredientCriteria.getPrice())//
                .setImage(ingredientCriteria.getImage())//
                .setQuantity(ingredientCriteria.getQuantity())//
                .setUnit(ingredientCriteria.getUnit())//
                .setDescription(ingredientCriteria.getDescription())//
                .setStatus(ingredientCriteria.getStatus())//
                .build();
    }

}
