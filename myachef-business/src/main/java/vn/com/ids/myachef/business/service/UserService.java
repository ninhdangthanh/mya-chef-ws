package vn.com.ids.myachef.business.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import vn.com.ids.myachef.business.dto.UserDTO;
import vn.com.ids.myachef.business.payload.request.ProfileRequest;
import vn.com.ids.myachef.dao.criteria.UserCriteria;
import vn.com.ids.myachef.dao.model.UserModel;

public interface UserService extends IGenericService<UserModel, Long> {

    public Page<UserModel> findAll(UserCriteria userCriteria);

    public UserDTO create(@Valid UserDTO userRequest, MultipartFile image);

    public UserDTO update(UserDTO userRequest, UserModel userModel, MultipartFile image);

    public UserModel findByUsername(String username);
}
