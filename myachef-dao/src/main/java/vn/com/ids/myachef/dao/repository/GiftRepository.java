package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.GiftModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface GiftRepository extends GenericRepository<GiftModel, Long>, JpaSpecificationExecutor<GiftModel> {

}
