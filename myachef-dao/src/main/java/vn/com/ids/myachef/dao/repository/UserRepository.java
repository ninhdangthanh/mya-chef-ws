package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.UserModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface UserRepository extends GenericRepository<UserModel, Long>, JpaSpecificationExecutor<UserModel> {

    UserModel findByUsername(String username);
}
