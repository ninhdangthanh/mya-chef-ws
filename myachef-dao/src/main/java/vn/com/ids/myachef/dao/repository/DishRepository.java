package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface DishRepository extends GenericRepository<DishModel, Long>, JpaSpecificationExecutor<DishModel> {

}
