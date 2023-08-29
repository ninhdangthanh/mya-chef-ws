package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.HomeConfigModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface HomeConfigRepository extends GenericRepository<HomeConfigModel, Long>, JpaSpecificationExecutor<HomeConfigModel> {

}
