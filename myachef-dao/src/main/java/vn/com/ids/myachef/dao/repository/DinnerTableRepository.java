package vn.com.ids.myachef.dao.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.model.DinnerTableModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface DinnerTableRepository extends GenericRepository<DinnerTableModel, Long>, JpaSpecificationExecutor<DinnerTableModel> {

}
