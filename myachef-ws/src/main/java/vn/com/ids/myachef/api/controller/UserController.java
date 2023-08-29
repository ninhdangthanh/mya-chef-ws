package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import vn.com.ids.myachef.business.converter.UserConverter;
import vn.com.ids.myachef.business.dto.UserDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.business.validation.group.OnUpdate;
import vn.com.ids.myachef.dao.criteria.UserCriteria;
import vn.com.ids.myachef.dao.enums.UserStatus;
import vn.com.ids.myachef.dao.model.UserModel;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private SecurityContextService securityContextService;

    @Operation(summary = "Find by criteria")
    @GetMapping("/search")
    public Page<UserDTO> findBycriteria(@ParameterObject UserCriteria userCriteria) {
        log.info("------------------ UserController - Find by criteria - START ----------------");
        Page<UserModel> page = userService.findAll(userCriteria);
        List<UserDTO> userDTOs = userConverter.toDTOs(page.getContent());
        Pageable pageable = PageRequest.of(userCriteria.getPageIndex(), userCriteria.getPageSize());
        log.info("------------------ UserController - Find by criteria - END ----------------");
        return new PageImpl<>(userDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public UserDTO findbyId(@PathVariable("id") Long id) {
        log.info("------------------ UserController - findById - START ----------------");
        UserModel userModel = userService.findOne(id);
        if (userModel == null) {
            throw new ResourceNotFoundException("Not found user with id: " + id);
        }
        log.info("------------------ UserController - findById - END ----------------");
        return userConverter.toDTO(userModel);
    }

    @Operation(summary = "Create")
    @PostMapping
    @Validated(OnCreate.class)
    public UserDTO create(@Valid @RequestBody UserDTO userRequest) {
        log.info("------------------ UserController - Create - START ----------------");
        UserDTO userDTOResponse = userService.create(userRequest);
        log.info("------------------ UserController - Create - END ----------------");
        return userDTOResponse;
    }

    @Operation(summary = "Update")
    @PatchMapping
    @Validated(OnUpdate.class)
    public UserDTO update(@Valid @RequestBody UserDTO userRequest) {
        log.info("------------------ UserController - Update - START ----------------");
        UserDTO userDTOResponse = userService.update(userRequest);
        log.info("------------------ UserController - Update - END ----------------");
        return userDTOResponse;
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam List<Long> ids) {
        log.info("------------------ UserController - Delete - START ----------------");
        if (!CollectionUtils.isEmpty(ids)) {
            userService.deleteByIds(ids);
        }
        log.info("------------------ UserController - Delete - END ----------------");
    }

    @Operation(summary = "Find User Info")
    @GetMapping("/user-info")
    public UserDTO findUserInfo() {
        log.info("------------------ UserController - findUserInfo - START ----------------");
        UserModel userModel = userService.findOne(securityContextService.getAuthenticatedUserId());
        if (userModel == null || userModel.getStatus() != UserStatus.ACTIVE) {
            throw new ResourceNotFoundException(String.format("Not found user by id: %s", securityContextService.getAuthenticatedUserId()));
        }
        log.info("------------------ UserController - findUserInfo - END ----------------");
        return userConverter.toDTO(userModel);
    }

}
