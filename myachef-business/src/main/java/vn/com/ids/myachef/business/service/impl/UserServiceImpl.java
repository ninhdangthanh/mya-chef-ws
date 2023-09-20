package vn.com.ids.myachef.business.service.impl;

import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.converter.UserConverter;
import vn.com.ids.myachef.business.dto.UserDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.business.service.ZaloSocialService;
import vn.com.ids.myachef.business.service.filehelper.FileStorageService;
import vn.com.ids.myachef.dao.criteria.UserCriteria;
import vn.com.ids.myachef.dao.criteria.builder.UserSpecificationBuilder;
import vn.com.ids.myachef.dao.enums.UserStatus;
import vn.com.ids.myachef.dao.model.UserModel;
import vn.com.ids.myachef.dao.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl extends AbstractService<UserModel, Long> implements UserService {

    @Autowired
    private UserConverter userConverter;
    
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private ZaloSocialService zaloSocialService;

    private UserRepository userRepository;

    protected UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    public Specification<UserModel> buildSpecification(UserCriteria userCriteria) {
        return (root, criteriaQuery, criteriaBuilder) //
        -> new UserSpecificationBuilder(root, criteriaBuilder) //
                .setRole(userCriteria.getRole())//
                .setStatus(userCriteria.getStatus())//
                .setSearchText(userCriteria.getSearchText())//
                .setTimeFrame(userCriteria.getFrom(), userCriteria.getTo())//
                .build();
    }

    @Override
    public Page<UserModel> findAll(UserCriteria userCriteria) {
        Specification<UserModel> specification = buildSpecification(userCriteria);
        Pageable pageable = buildPageable(userCriteria);
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public UserDTO create(@Valid UserDTO userRequest, MultipartFile image) {
        UserModel userModel = userConverter.toModel(userRequest);
        
        if (image != null) {
            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullUserPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullUserPath(), prefixName), generatedName, image);
            userModel.setAvatarFile(generatedName);
        }
        
        userModel = userRepository.save(userModel);
        return userConverter.toDTO(userModel);
    }

    @Override
    public UserDTO update(UserDTO userRequest, UserModel userModel, MultipartFile image) {
        if (image != null) {
            if (StringUtils.hasText(userModel.getAvatarFile())) {
                fileStorageService.delete(applicationConfig.getFullUserPath() + userModel.getAvatarFile());
            }

            String prefixName = UUID.randomUUID().toString();
            String generatedName = String.format("%s%s%s", prefixName, "-", image.getOriginalFilename());
            fileStorageService.upload(applicationConfig.getFullUserPath(), generatedName, image);
            fileStorageService.upload(String.format(applicationConfig.getFullUserPath(), prefixName), generatedName, image);
            userModel.setAvatarFile(generatedName);
        }

        userConverter.mapDataToUpdate(userModel, userRequest);
        userModel = userRepository.save(userModel);
        return userConverter.toDTO(userModel);
    }

    @Override
    public UserModel findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
