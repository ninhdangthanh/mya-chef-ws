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
import vn.com.ids.myachef.business.converter.UserConverter;
import vn.com.ids.myachef.business.dto.DishDTO;
import vn.com.ids.myachef.business.dto.UserDTO;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.DishService;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.DishCriteria;
import vn.com.ids.myachef.dao.criteria.UserCriteria;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.model.UserModel;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

//    @Operation(summary = "Find by criteria")
//    @GetMapping("/search")
//    public Page<UserDTO> getAll(@ParameterObject UserCriteria userCriteria) {
//        Page<UserModel> page = userService.findAll(userCriteria);
//        List<UserDTO> userDTOs = userConverter.toBasicDTOs(page.getContent());
//        Pageable pageable = PageRequest.of(userCriteria.getPageIndex(), userCriteria.getPageSize());
//        return new PageImpl<>(userDTOs, pageable, page.getTotalElements());
//    }
//
//    @Operation(summary = "Find by id")
//    @GetMapping("/{id}")
//    public UserDTO findById(@PathVariable Long id) {
//        UserModel userModel = userService.findOne(id);
//        if (userModel == null) {
//            throw new ResourceNotFoundException("Not found user with id: " + id);
//        }
//        return userConverter.toBasicDTO(userModel);
//    }
//
    @Operation(summary = "Create")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Validated(OnCreate.class)
    public UserDTO create(@Valid @RequestPart UserDTO userDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
        return userService.create(userDTO, image);
    }
//
//    @Operation(summary = "Update")
//    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public UserDTO update(@PathVariable Long id, @Valid @RequestPart UserDTO userDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
//        UserModel userModel = userService.findOne(id);
//        if (userModel == null) {
//            throw new ResourceNotFoundException("Not found user with id: " + id);
//        }
//        return userService.update(userDTO, userModel, image);
//    }
//
//    @Operation(summary = "Delete")
//    @DeleteMapping
//    public void delete(@RequestParam Long id) {
//        userService.deleteById(id);
//    }

}
