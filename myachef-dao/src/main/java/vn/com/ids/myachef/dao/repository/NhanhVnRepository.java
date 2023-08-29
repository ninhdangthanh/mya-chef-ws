package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.model.NhanhVnModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface NhanhVnRepository extends GenericRepository<NhanhVnModel, Long>, JpaSpecificationExecutor<NhanhVnModel> {

    @Query(value = "SELECT * FROM `nhanh_vn` ORDER BY `priority` DESC LIMIT 1", nativeQuery = true)
    NhanhVnModel findByBiggestPriority();

}
