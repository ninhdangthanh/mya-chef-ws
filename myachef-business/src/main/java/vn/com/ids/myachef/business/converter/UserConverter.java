package vn.com.ids.myachef.business.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vn.com.ids.myachef.business.config.ApplicationConfig;
import vn.com.ids.myachef.business.dto.UserDTO;
import vn.com.ids.myachef.business.service.FileUploadService;
import vn.com.ids.myachef.dao.enums.UserStatus;
import vn.com.ids.myachef.dao.model.UserModel;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BCryptPasswordEncoder encoder;
    
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ApplicationConfig applicationConfig;

    public UserDTO toBasicDTO(UserModel userModel) {
        UserDTO userDTO = mapper.map(userModel, UserDTO.class);
        if (StringUtils.hasText(userDTO.getAvatarFile()) && !userDTO.getAvatarFile().startsWith("https://pos.nvncdn.net")) {
            userDTO.setAvatarFile(fileUploadService.getFilePath(applicationConfig.getUserPath(), userDTO.getAvatarFile()));
        }
        return userDTO;
    }

    public List<UserDTO> toBasicDTOs(List<UserModel> userModels) {
        if (CollectionUtils.isEmpty(userModels)) {
            return new ArrayList<>();
        }

        List<UserDTO> userDTOs = new ArrayList<>();
        for (UserModel userModel : userModels) {
            userDTOs.add(toBasicDTO(userModel));
        }

        return userDTOs;
    }

    public UserDTO toDTO(UserModel userModel) {
        return toBasicDTO(userModel);
    }

    public List<UserDTO> toDTOs(List<UserModel> userModels) {
        if (CollectionUtils.isEmpty(userModels)) {
            return new ArrayList<>();
        }

        List<UserDTO> userDTOs = new ArrayList<>();
        for (UserModel userModel : userModels) {
            userDTOs.add(toDTO(userModel));
        }

        return userDTOs;
    }

    public UserModel toModel(UserDTO userDTO) {
        UserModel userModel = mapper.map(userDTO, UserModel.class);
        userModel.setPassword(encoder.encode(userDTO.getPassword()));
        // userModel.setRole(UserRole.USER);
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSearchText(buildSearchText(userModel));
        return userModel;
    }

    public void mapDataToUpdate(UserModel userModel, UserDTO userRequest) {
        if (userRequest.getRole() != null) {
            userModel.setRole(userRequest.getRole());
        }

        if (userRequest.getFullName() != null) {
            userModel.setFullName(userRequest.getFullName());
        }

        if (userRequest.getPhoneNumber() != null) {
            userModel.setPhoneNumber(userRequest.getPhoneNumber());
        }

        if (userRequest.getAddress() != null) {
            userModel.setAddress(userRequest.getAddress());
        }

        if (userRequest.getBirthday() != null) {
            userModel.setBirthday(userRequest.getBirthday());
        }
        
        if (userRequest.getPassword() != null) {
        	userModel.setPassword(encoder.encode(userRequest.getPassword()));
        }

        if (userRequest.getStatus() != null) {
            userModel.setStatus(userRequest.getStatus());
        }

        userModel.setSearchText(buildSearchText(userModel));

    }

    private String buildSearchText(UserModel userModel) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.hasText(userModel.getFullName()) ? userModel.getFullName().trim() + " " : "");
        sb.append(StringUtils.hasText(userModel.getPhoneNumber()) ? userModel.getPhoneNumber().trim() + " " : "");
        sb.append(StringUtils.hasText(userModel.getAddress()) ? userModel.getAddress().trim() + " " : "");
        return sb.toString();
    }

}
