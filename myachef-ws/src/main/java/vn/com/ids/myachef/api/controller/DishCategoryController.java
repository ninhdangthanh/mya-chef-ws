package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.DishCategoryConverter;
import vn.com.ids.myachef.business.converter.IngredientCategoryConverter;
import vn.com.ids.myachef.business.dto.DishCategoryDTO;
import vn.com.ids.myachef.business.dto.IngredientCategoryDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.DishCategoryService;
import vn.com.ids.myachef.business.service.DishService;
import vn.com.ids.myachef.business.service.IngredientCategoryService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.model.DishCategoryModel;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.model.IngredientCategoryModel;

@RestController
@RequestMapping("/api/dish-category")
public class DishCategoryController {
	
	@Autowired
	private DishCategoryService dishCategoryService;
	
	@Autowired
	private DishCategoryConverter dishCategoryConverter;

	@Autowired
    private DishService dishService;
	
	@Operation(summary = "Get all")
    @GetMapping("/search")
	public List<DishCategoryDTO> getAll() {
		return dishCategoryConverter.toBasicDTOs(dishCategoryService.findAll());
	}
	
	@Operation(summary = "Find by id")
    @GetMapping("/{id}")
	public DishCategoryDTO findById(@PathVariable Long id) {
	    DishCategoryModel ingredientCategoryModel = dishCategoryService.findOne(id);
		if(ingredientCategoryModel == null) {
			throw new ResourceNotFoundException("Not found dish category with id: " + id);
		}
		return dishCategoryConverter.toDTO(ingredientCategoryModel);
	}
	
	@Operation(summary = "Create")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Validated(OnCreate.class)
	public DishCategoryDTO create(@Valid @RequestPart DishCategoryDTO dishCategoryDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
		return dishCategoryService.create(dishCategoryDTO, image);
	}
	
	@Operation(summary = "Update")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DishCategoryDTO updateBanner(@PathVariable Long id, @Valid @RequestPart DishCategoryDTO dishCategoryDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
	    DishCategoryModel dishCategoryModel = dishCategoryService.findOne(id);
        if (dishCategoryModel == null) {
            throw new ResourceNotFoundException("Not found dish category with id: " + id);
        }
        return dishCategoryService.update(dishCategoryDTO, dishCategoryModel, image);
    }
	
	@Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam Long id) {
		List<DishModel> dishModels = dishService.findByDishCategoryId(id);
	    if(!CollectionUtils.isEmpty(dishModels)) {
	        throw new BadRequestException("Vui lòng xóa hết các món ăn để xóa danh mục");
	    }
	    dishCategoryService.deleteById(id);
	}
}
