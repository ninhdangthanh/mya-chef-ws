package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import vn.com.ids.myachef.business.converter.IngredientCategoryConverter;
import vn.com.ids.myachef.business.dto.IngredientCategoryDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.IngredientCategoryService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.model.IngredientCategoryModel;

@RestController
@RequestMapping("/api/ingredient-category")
@Slf4j
public class IngredientCategoryController {
	
	@Autowired
	private IngredientCategoryService ingredientCategoryService;
	
	@Autowired
	private IngredientCategoryConverter ingredientCategoryConverter;
	
	@Operation(summary = "Get all")
    @GetMapping("/search")
	public List<IngredientCategoryDTO> getAll() {
		return ingredientCategoryConverter.toBasicDTOs(ingredientCategoryService.findAll());
	}
	
	@Operation(summary = "Find by id")
    @GetMapping("/{id}")
	public IngredientCategoryDTO findById(@PathVariable Long id) {
		IngredientCategoryModel ingredientCategoryModel = ingredientCategoryService.findOne(id);
		if(ingredientCategoryModel == null) {
			throw new ResourceNotFoundException("Not found ingredient category with id: " + id);
		}
		return ingredientCategoryConverter.toDTO(ingredientCategoryModel);
	}
	
	@Operation(summary = "Create")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Validated(OnCreate.class)
	public IngredientCategoryDTO create(@Valid @RequestPart IngredientCategoryDTO ingredientCategoryDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
		return ingredientCategoryService.create(ingredientCategoryDTO, image);
	}
	
	@Operation(summary = "Update")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public IngredientCategoryDTO updateBanner(@PathVariable Long id, @Valid @RequestPart IngredientCategoryDTO ingredientCategoryDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
        IngredientCategoryModel ingredientCategoryModel = ingredientCategoryService.findOne(id);
        if (ingredientCategoryModel == null) {
            throw new ResourceNotFoundException("Not found ingredient category with id: " + id);
        }
        return ingredientCategoryService.update(ingredientCategoryDTO, ingredientCategoryModel, image);
    }
	
	@Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam Long id) {
		ingredientCategoryService.deleteById(id);
	}
}
