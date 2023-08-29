package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.CartModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;
import vn.com.ids.myachef.dao.repository.extended.model.CartProduct;

public interface CartRepository extends GenericRepository<CartModel, Long>, JpaSpecificationExecutor<CartModel> {

    CartModel findByUserIdAndNhanhVnProductId(Long userId, String nhanhVnProductId);

    @Query(value = "SELECT c.quantity, p FROM CartModel c LEFT JOIN ProductConfigModel p ON c.nhanhVnProductId = p.nhanhVnId WHERE c.id IN ?1")
    public List<Object[]> findQuantityAndProductByCartIds(List<Long> cartIds);

    List<CartModel> findByUserId(Long userId);
    
    @Query(value = "SELECT new vn.com.ids.myachef.dao.repository.extended.model.CartProduct(p, c.quantity, c.preSelect) FROM CartModel c LEFT JOIN ProductConfigModel p ON c.nhanhVnProductId = p.nhanhVnId WHERE c.id IN ?1")
    public List<CartProduct> findQuantityAndProductByCartIdIn(List<Long> cartIds);

     @Modifying
     @Query(value = "UPDATE CartModel cart SET cart.preSelect = false WHERE cart.id IN ?1")
     public void updatePreSelectFalseByIdIn(List<Long> cartIds);

}
