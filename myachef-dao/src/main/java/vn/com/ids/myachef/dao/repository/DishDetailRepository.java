package vn.com.ids.myachef.dao.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.DishDetailModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

@Transactional
public interface DishDetailRepository extends GenericRepository<DishDetailModel, Long>, JpaSpecificationExecutor<DishDetailModel> {
    @Query(value = "DELETE FROM `dish_detail` WHERE `dish_id` = ?1", nativeQuery = true) 
    @Modifying
    void deleteByDishId(Long id);
    
    @Query(value = "SELECT dd.* FROM `dish_detail` as dd WHERE `dish_id` = ?1", nativeQuery = true) 
    List<DishDetailModel> findByDishId(Long id);
}
