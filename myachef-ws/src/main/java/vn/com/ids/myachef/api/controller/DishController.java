package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import vn.com.ids.myachef.business.converter.DishConverter;
import vn.com.ids.myachef.business.dto.DishDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.DishService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.DishCriteria;
import vn.com.ids.myachef.dao.model.DishModel;

@RestController
@RequestMapping("/api/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishConverter dishConverter;

    @Operation(summary = "Find by criteria")
    @GetMapping("/search")
    public Page<DishDTO> getAll(@ParameterObject DishCriteria dishCriteria) {
        Page<DishModel> page = dishService.findAll(dishCriteria);
        List<DishDTO> dishDTOs = dishConverter.toBasicDTOs(page.getContent());
        Pageable pageable = PageRequest.of(dishCriteria.getPageIndex(), dishCriteria.getPageSize());
        return new PageImpl<>(dishDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public DishDTO findById(@PathVariable Long id) {
        DishModel dishModel = dishService.findOne(id);
        if (dishModel == null) {
            throw new ResourceNotFoundException("Not found dish with id: " + id);
        }
        return dishConverter.toBasicDTO(dishModel);
    }

    @Operation(summary = "Create")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Validated(OnCreate.class)
    public DishDTO create(@Valid @RequestPart DishDTO dishDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
        return dishService.create(dishDTO, image);
    }

    @Operation(summary = "Update")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DishDTO update(@PathVariable Long id, @Valid @RequestPart DishDTO dishDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
        DishModel dishModel = dishService.findOne(id);
        if (dishModel == null) {
            throw new ResourceNotFoundException("Not found dish with id: " + id);
        }
        return dishService.update(dishDTO, dishModel, image);
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam Long id) {
        dishService.deleteById(id);
    }

}
