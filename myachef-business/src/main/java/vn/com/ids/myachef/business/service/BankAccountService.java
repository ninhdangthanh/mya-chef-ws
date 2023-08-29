package vn.com.ids.myachef.business.service;

import java.util.List;

import vn.com.ids.myachef.business.dto.BankAccountDTO;
import vn.com.ids.myachef.dao.enums.Status;
import vn.com.ids.myachef.dao.model.BankAccountModel;

public interface BankAccountService extends IGenericService<BankAccountModel, Long> {

    List<BankAccountDTO> findAllByStatus(Status status);

    BankAccountDTO create(BankAccountDTO bankAccountDTO);

    BankAccountDTO update(BankAccountModel bankAccountModel, BankAccountDTO bankAccountDTO);

}
