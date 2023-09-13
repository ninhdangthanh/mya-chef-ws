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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import vn.com.ids.myachef.business.converter.OrderConverter;
import vn.com.ids.myachef.business.dto.OrderDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.OrderService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.OrderCriteria;
import vn.com.ids.myachef.dao.model.OrderModel;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderConverter orderConverter;

    @Operation(summary = "Find by criteria")
    @GetMapping("/search")
    public Page<OrderDTO> getAll(@ParameterObject OrderCriteria orderCriteria) {
        Page<OrderModel> page = orderService.findAll(orderCriteria);
        List<OrderDTO> orderDTOs = orderConverter.toBasicDTOs(page.getContent());
        Pageable pageable = PageRequest.of(orderCriteria.getPageIndex(), orderCriteria.getPageSize());
        return new PageImpl<>(orderDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public OrderDTO findById(@PathVariable Long id) {
        OrderModel orderModel = orderService.findOne(id);
        if (orderModel == null) {
            throw new ResourceNotFoundException("Not found order with id: " + id);
        }
        return orderConverter.toBasicDTO(orderModel);
    }

    @Operation(summary = "Create")
    @PostMapping
    @Validated(OnCreate.class)
    public OrderDTO create(@Valid @RequestBody OrderDTO orderDTO) {
        
        return null;
//        return orderService.create(orderDTO);
    }

    @Operation(summary = "Update")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OrderDTO update(@PathVariable Long id, @Valid @RequestPart OrderDTO orderDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
        OrderModel orderModel = orderService.findOne(id);
        if (orderModel == null) {
            throw new ResourceNotFoundException("Not found order with id: " + id);
        }
        return orderService.update(orderDTO, orderModel, image);
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam Long id) {
        orderService.deleteById(id);
    }

}
