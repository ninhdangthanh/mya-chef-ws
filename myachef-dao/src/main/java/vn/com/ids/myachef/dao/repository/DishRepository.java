package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.DishModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface DishRepository extends GenericRepository<DishModel, Long>, JpaSpecificationExecutor<DishModel> {
    @Query(value = "SELECT * FROM `dish` WHERE `dish_category_id` = ?1", nativeQuery = true)
    List<DishModel> findByDishCategoryId(Long id);
}
