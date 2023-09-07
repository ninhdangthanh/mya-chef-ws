package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.DishCategoryModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface DishCategoryRepository extends GenericRepository<DishCategoryModel, Long>, JpaSpecificationExecutor<DishCategoryModel> {

}
