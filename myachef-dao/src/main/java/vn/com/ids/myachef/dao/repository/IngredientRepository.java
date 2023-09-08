package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.IngredientModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface IngredientRepository extends GenericRepository<IngredientModel, Long>, JpaSpecificationExecutor<IngredientModel> {
	List<IngredientModel> findByIdIn(List<Long> ids);
}
