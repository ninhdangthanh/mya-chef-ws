package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.DishDetailModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface DishDetailRepository extends GenericRepository<DishDetailModel, Long>, JpaSpecificationExecutor<DishDetailModel> {

}
