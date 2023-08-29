package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.enums.SalePackageDetailType;
import vn.com.ids.myachef.dao.model.SalePackageDetailModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface SalePackageDetailRepository extends GenericRepository<SalePackageDetailModel, Long>, JpaSpecificationExecutor<SalePackageDetailModel>  {
    List<SalePackageDetailModel> findByCustomerIdAndType(Long customerId, SalePackageDetailType salePackageDetailType);

}
