package vn.com.ids.myachef.business.service.impl;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.com.ids.myachef.business.converter.UserConverter;
import vn.com.ids.myachef.business.dto.UserDTO;
import vn.com.ids.myachef.business.exception.error.BadRequestException;
import vn.com.ids.myachef.business.exception.error.ResourceNotFoundException;
import vn.com.ids.myachef.business.payload.request.ProfileRequest;
import vn.com.ids.myachef.business.service.UserService;
import vn.com.ids.myachef.business.service.ZaloSocialService;
import vn.com.ids.myachef.business.zalo.social.UserProfile;
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
    public UserDTO create(@Valid UserDTO userRequest) {
        UserModel userModel = userRepository.findByUsername(userRequest.getUsername());
        if (userModel == null) {
            userModel = new UserModel();
        } else if (UserStatus.DELETED == userModel.getStatus()) {
            userRequest.setId(userModel.getId());
        } else {
            throw new BadRequestException("Username used");
        }
        userModel = userConverter.toModel(userRequest);
        userModel = userRepository.save(userModel);
        return userConverter.toDTO(userModel);
    }

    @Override
    public UserDTO update(UserDTO userRequest) {
        UserModel userModel = findOne(userRequest.getId());
        if (userModel == null) {
            throw new ResourceNotFoundException(String.format("Not found User by id: %s", userRequest.getId()));
        }

        userConverter.mapDataToUpdate(userModel, userRequest);
        userModel = userRepository.save(userModel);
        return userConverter.toDTO(userModel);
    }

    @Override
    public UserModel findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDTO updateProfile(ProfileRequest profileRequest, Long authenticatedUserId) {
        UserModel userModel = findOne(authenticatedUserId);
        if (userModel == null) {
            throw new ResourceNotFoundException(String.format("Not found User by id: %s", authenticatedUserId));
        }

        if (profileRequest.getToken() != null) {
            UserProfile userProfileResponse = zaloSocialService.getProfile(userModel.getZaloAccessToken(), profileRequest.getToken());
            if (userProfileResponse == null || userProfileResponse.getData() == null) {
                throw new ResourceNotFoundException("Can not get zalo user phoneNumber or location by token");
            }

            if (userProfileResponse.getData().getNumber() != null) {
                userModel.setPhoneNumber(userProfileResponse.getData().getNumber());
                userModel = save(userModel);
            }
        } else {
            throw new BadRequestException("Not support for all code, phoneNumber, location");
        }


        return userConverter.toDTO(userModel);
    }

}
