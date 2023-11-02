package vn.com.ids.myachef.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
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
import vn.com.ids.myachef.api.security.jwt.JWTTokenService;
import vn.com.ids.myachef.api.security.userdetails.UserDetailsImpl;
import vn.com.ids.myachef.business.converter.DishConverter;
import vn.com.ids.myachef.business.converter.UserConverter;
import vn.com.ids.myachef.business.dto.DishDTO;
import vn.com.ids.myachef.business.dto.UserDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.DishService;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.business.validation.group.OnCreate;
import vn.com.ids.myachef.dao.criteria.DishCriteria;
import vn.com.ids.myachef.dao.criteria.UserCriteria;
import vn.com.ids.myachef.dao.enums.UserRole;
import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.model.UserModel;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;
    
    @Autowired
    private JWTTokenService jwtTokenService;
    
    @Autowired
    private BCryptPasswordEncoder encoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(summary = "Find by criteria")
    @GetMapping("/search")
    public Page<UserDTO> getAll(@ParameterObject UserCriteria userCriteria) {
        Page<UserModel> page = userService.findAll(userCriteria);
        List<UserDTO> userDTOs = userConverter.toBasicDTOs(page.getContent());
        Pageable pageable = PageRequest.of(userCriteria.getPageIndex(), userCriteria.getPageSize());
        return new PageImpl<>(userDTOs, pageable, page.getTotalElements());
    }

    @Operation(summary = "Find by id")
    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable Long id) {
        UserModel userModel = userService.findOne(id);
        if (userModel == null) {
            throw new ResourceNotFoundException("Not found user with id: " + id);
        }
        return userConverter.toBasicDTO(userModel);
    }

    @Operation(summary = "Create")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Validated(OnCreate.class)
    public UserDTO create(@Valid @RequestPart UserDTO userDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
        return userService.create(userDTO, image);
    }

    @Operation(summary = "Update")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDTO update(@PathVariable Long id, @Valid @RequestPart UserDTO userDTO, @RequestParam(value = "image", required = false) MultipartFile image) {
        UserModel userModel = userService.findOne(id);
        if (userModel == null) {
            throw new ResourceNotFoundException("Not found user with id: " + id);
        }
        return userService.update(userDTO, userModel, image);
    }
    
    @Operation(summary = "Change Password")
    @PatchMapping(value = "/change-password")
    public UserDTO changepassword(HttpServletRequest request, @Valid @RequestBody UserDTO userDTO) {
        if(userDTO.getId() == null || userDTO.getId() < 0 || !StringUtils.hasText(userDTO.getNewPassword())) {
            throw new BadRequestException("You must provide user id and new password");
        }
        String jwtToken = jwtTokenService.parseJwt(request);
        Long userId = jwtTokenService.getUserId(jwtToken);
        UserModel admin = userService.findOne(userId);
        UserModel userChangePassword = userService.findOne(userDTO.getId());
        
        if(admin == null || userChangePassword == null) {
            throw new BadRequestException("You must provide token of admin and correc user change password id!");
        }
        
        if(admin.getRole() != UserRole.ADMIN) {
            throw new BadRequestException("Not have permision to change password");
        }
        
        userChangePassword.setPassword(encoder.encode(userDTO.getNewPassword()));
        
        userService.save(userChangePassword);
        
        return userDTO;
    }

    @Operation(summary = "Delete")
    @DeleteMapping
    public void delete(@RequestParam Long id) {
        userService.deleteById(id);
    }

}
