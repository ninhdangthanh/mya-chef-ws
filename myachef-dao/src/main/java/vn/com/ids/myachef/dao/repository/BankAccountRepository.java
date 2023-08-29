package vn.com.ids.myachef.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.BankAccountModel;
import vn.com.ids.myachef.dao.repository.extended.GenericRepository;

public interface BankAccountRepository extends GenericRepository<BankAccountModel, Long>, JpaSpecificationExecutor<BankAccountModel> {

    List<BankAccountModel> findByStatus(Status status);

}
