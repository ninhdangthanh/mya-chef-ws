package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.SystemConfigModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface SystemConfigRepository extends GenericRepository<SystemConfigModel, Long>, JpaSpecificationExecutor<SystemConfigModel> {

}
