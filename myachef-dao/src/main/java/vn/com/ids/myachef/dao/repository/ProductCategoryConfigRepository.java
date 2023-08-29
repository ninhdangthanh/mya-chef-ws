package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.ProductCategoryConfigModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface ProductCategoryConfigRepository
        extends GenericRepository<ProductCategoryConfigModel, Long>, JpaSpecificationExecutor<ProductCategoryConfigModel> {

    @Query("SELECT c FROM ProductCategoryConfigModel c WHERE LOWER(c.name) != 'combo'")
    List<ProductCategoryConfigModel> findAllIgnoreCombo();

    @Query("SELECT c FROM ProductCategoryConfigModel c WHERE LOWER(c.name) = 'combo'")
    ProductCategoryConfigModel findCombo();

    @Query(value = "SELECT p.nhanhVnId FROM ProductCategoryConfigModel p")
    List<String> getAllNhanhIds();

    ProductCategoryConfigModel findByNhanhVnId(String nhanhVnProductCategoryId);
}
