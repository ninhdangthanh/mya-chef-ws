package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.SecurityContextService;
import vn.com.ids.myachef.business.dto.CartDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.CartService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.CartCriteria;
import vn.com.ids.myachef.dao.model.CartModel;

@RestController
@RequestMapping("/api/carts")
@Slf4j
@Validated
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private SecurityContextService securityContextService;

    @Operation(summary = "Search criteria")
    @GetMapping("/search")
    public Page<CartDTO> findByCriteria(@ParameterObject CartCriteria cartCriteria) {
        log.info("------------------ Find by criteria - START ----------------");
        cartCriteria.setUserId(securityContextService.getAuthenticatedUserId());
        return cartService.findByCriteria(cartCriteria, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Create cart")
    @PostMapping
    @Validated(OnCreate.class)
    public CartDTO create(@Valid @RequestBody CartDTO cartDTO) {
        log.info("------------------ Create - START ----------------");
        cartDTO.setUserId(securityContextService.getAuthenticatedUserId());
        return cartService.create(cartDTO, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Update cart")
    @PatchMapping("/{id}")
    public CartDTO update(@PathVariable Long id, @RequestBody CartDTO cartDTO) {
        log.info("------------------ update - START ----------------");
        CartModel cartModel = cartService.findOne(id);
        if (cartModel == null) {
            throw new ResourceNotFoundException("Not found cart with id: " + id);
        }
        return cartService.update(cartModel, cartDTO, securityContextService.getAuthenticatedUserId());
    }

    @Operation(summary = "Delete cart")
    @DeleteMapping
    public void delete(@RequestParam List<Long> ids) {
        log.info("ids: {}", ids);
        if (!CollectionUtils.isEmpty(ids)) {
            cartService.deleteByIds(ids);
        }

        log.info("------------------ Delete - END ----------------");
    }
}
