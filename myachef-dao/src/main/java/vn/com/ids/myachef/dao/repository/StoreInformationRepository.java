package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.StoreInformationModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface StoreInformationRepository extends GenericRepository<StoreInformationModel, Long>, JpaSpecificationExecutor<StoreInformationModel> {

    public StoreInformationModel findByIsDefaultTrue();

}
