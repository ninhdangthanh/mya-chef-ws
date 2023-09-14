package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import vn.com.ids.myachef.business.converter.DinnerTableConverter;
import vn.com.ids.myachef.business.dto.DinnerTableDTO;
import vn.com.ids.myachef.business.dto.DishDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.DinnerTableService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.model.DinnerTableModel;

@RestController
@RequestMapping("/api/dinner-table")
public class DinnerTableController {

    @Autowired
    private DinnerTableService dinnerTableService;

    @Autowired
    private DinnerTableConverter dinnerTableConverter;

    @Operation(summary = "Get all")
    @GetMapping("/search")
    public List<DinnerTableDTO> getAll() {
        return dinnerTableConverter.toBasicDTOs(dinnerTableService.findAll());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public DinnerTableDTO findById(@PathVariable Long id) {
        DinnerTableModel dinnerTableModel = dinnerTableService.findOne(id);
        if (dinnerTableModel == null) {
            throw new ResourceNotFoundException("Not found dinner table with id: " + id);
        }
        return dinnerTableConverter.toDTO(dinnerTableModel);
    }

    @Operation(summary = "Create")
    @PostMapping
    @Validated(OnCreate.class)
    public DinnerTableDTO create(@Valid @RequestBody DinnerTableDTO dinnerTableDTO) {
        return dinnerTableService.create(dinnerTableDTO);
    }

    @Operation(summary = "Update")
    @PatchMapping(value = "/{id}")
    public DinnerTableDTO updateBanner(@PathVariable Long id, @Valid @RequestBody DinnerTableDTO dinnerTableDTO) {
        DinnerTableModel dinnerTableModel = dinnerTableService.findOne(id);
        if (dinnerTableModel == null) {
            throw new ResourceNotFoundException("Not found dinner model with id: " + id);
        }
        return dinnerTableService.update(dinnerTableDTO, dinnerTableModel);
    }
    
    @Operation(summary = "Get Dish Existing In Dinner Table")
    @GetMapping(value = "/existing-dish/{id}")
    public List<DishDTO> getDishExistingInDinnerTable(@PathVariable Long id) {
        DinnerTableModel dinnerTableModel = dinnerTableService.findOne(id);
        if (dinnerTableModel == null) {
            throw new ResourceNotFoundException("Not found dinner model with id: " + id);
        }
        return dinnerTableService.getDishExistingInDinnerTable(id);
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam Long id) {
        dinnerTableService.deleteById(id);
    }
}
