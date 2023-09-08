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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.business.converter.IngredientConverter;
import vn.com.ids.myachef.business.dto.IngredientDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.IngredientService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.model.IngredientModel;

@RestController
@RequestMapping("/api/ingredient")
@Slf4j
public class IngredientController {
	
	@Autowired
	private IngredientService ingredientService;
	
	@Autowired
	private IngredientConverter ingredientConverter;
	
	@Operation(summary = "Get all")
    @GetMapping("/search")
	public List<IngredientDTO> getAll() {
		return ingredientConverter.toBasicDTOs(ingredientService.findAll());
	}
	
	@Operation(summary = "Find by id")
    @GetMapping("/{id}")
	public IngredientDTO findById(@PathVariable Long id) {
		IngredientModel ingredientModel = ingredientService.findOne(id);
		if(ingredientModel == null) {
			throw new ResourceNotFoundException("Not found ingredient with id: " + id);
		}
		return ingredientConverter.toBasicDTO(ingredientModel);
	}
	
	@Operation(summary = "Create")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Validated(OnCreate.class)
	public IngredientDTO create(@Valid @RequestPart IngredientDTO ingredientDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
		return ingredientService.create(ingredientDTO, image);
	}
	
	@Operation(summary = "Update")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public IngredientDTO update(@PathVariable Long id, @Valid @RequestPart IngredientDTO ingredientDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
        IngredientModel ingredientModel = ingredientService.findOne(id);
        if (ingredientModel == null) {
            throw new ResourceNotFoundException("Not found ingredient with id: " + id);
        }
        return ingredientService.update(ingredientDTO, ingredientModel, image);
    }
	
	@Operation(summary = "Update Quantity")
    @PatchMapping(value = "/manual-add-ingredient")
    public List<IngredientDTO> manualAddIngredient(@Valid @RequestBody List<IngredientDTO> ingredientDTOs) { //[{id_nguyên_liệu: quantity}, {id_nguyên_liệu: quantity}]
        return ingredientService.manualAddIngredient(ingredientDTOs);
    }
	
	@Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam Long id) {
		ingredientService.deleteById(id);
	}
}
