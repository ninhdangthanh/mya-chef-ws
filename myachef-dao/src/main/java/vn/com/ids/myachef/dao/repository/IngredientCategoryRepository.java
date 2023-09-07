package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.IngredientCategoryModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface IngredientCategoryRepository extends GenericRepository<IngredientCategoryModel, Long>, JpaSpecificationExecutor<IngredientCategoryModel> {

}
