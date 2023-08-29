package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.SalePackageModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface SalePackageRepository extends GenericRepository<SalePackageModel, Long>, JpaSpecificationExecutor<SalePackageModel> {

    @Query(value = "UPDATE `sale_package` SET `status` = 'IN_ACTIVE' WHERE `id` IN ?1", nativeQuery = true)
    @Modifying
    void updateStatusInActiveByIds(List<Long> ids);

    List<SalePackageModel> findByStatus(Status status);

}
